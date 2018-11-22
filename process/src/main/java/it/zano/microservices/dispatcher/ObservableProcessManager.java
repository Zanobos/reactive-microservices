package it.zano.microservices.dispatcher;

import it.zano.microservices.dispatcher.tasks.CreateEventTask;
import it.zano.microservices.dispatcher.tasks.PutDocumentCompletedEventTask;
import it.zano.microservices.event.observableprocess.ObservableProcessEventPublisher;
import it.zano.microservices.model.beans.ObservableProcess;
import it.zano.microservices.model.beans.ObservableProcessStateEnum;
import it.zano.microservices.webservices.documents.DocumentTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Service
public class ObservableProcessManager {

    private final static Logger logger = LoggerFactory.getLogger(ObservableProcessManager.class);

    private final DocumentTemplate documentTemplate;
    private final ObservableProcessStorage observableProcessStorage; //This must be shared between all the applications
    private final ExecutorService executorService;
    private final ObservableProcessEventPublisher eventPublisher;
    private final ConcurrentMap<Integer,ConcurrencyHelper> concurrencyHelperMap;
    private final ObservableProcessManagerProperties properties;

    @Autowired
    public ObservableProcessManager(ObservableProcessStorage observableProcessStorage,
                                    ObservableProcessEventPublisher eventPublisher,
                                    ObservableProcessManagerProperties properties,
                                    DocumentTemplate documentTemplate) {
        this.observableProcessStorage = observableProcessStorage;
        this.eventPublisher = eventPublisher;
        this.documentTemplate = documentTemplate;
        this.properties = properties;
        this.executorService = Executors.newWorkStealingPool();
        this.concurrencyHelperMap = new ConcurrentHashMap<>();
    }

    public ObservableProcess executeEvent(EventTypeEnum event, Integer processId) {

        //Add a new condition variable for this process
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(new ReentrantLock());
        ConcurrencyHelper oldValue = concurrencyHelperMap.putIfAbsent(processId, concurrencyHelper);
        if(oldValue != null)
            concurrencyHelper = oldValue;

        concurrencyHelper.getLock().lock();
        ObservableProcess observableProcess;
        try {
            //TODO controlla condizione di GO/ABORT
            logger.info("Start dealing with {} event", event);
            switch (event) {
                case CREATE:
                    observableProcess = new ObservableProcess();
                    observableProcess.setId(processId);
                    observableProcess.setState(ObservableProcessStateEnum.PUT_DOCUMENT_IN_PROGRESS);
                    observableProcess.setLastObservedState(ObservableProcessStateEnum.NOT_OBSERVED);
                    //Async call -> this call an external service
                    executorService.execute(new CreateEventTask(documentTemplate, processId, eventPublisher));
                    observableProcessStorage.saveProcess(observableProcess);
                    break;
                case PUT_DOCUMENT_COMPLETED:
                    observableProcess = observableProcessStorage.retrieveProcess(processId);
                    observableProcess.setState(ObservableProcessStateEnum.WAITED_IN_PROGRESS);
                    //Async call -> this just perform local stuff
                    executorService.execute(new PutDocumentCompletedEventTask(processId, eventPublisher));
                    observableProcessStorage.saveProcess(observableProcess);
                    break;
                case WAITED_COMPLETED:
                    observableProcess = observableProcessStorage.retrieveProcess(processId);
                    observableProcess.setState(ObservableProcessStateEnum.END);
                    //No call to service, just end the process
                    observableProcessStorage.saveProcess(observableProcess);
                    break;
                default:
                    //it should never happen
                    observableProcess = null;
            }
            concurrencyHelper.getCondition().signalAll();
        } finally {
            concurrencyHelper.getLock().unlock();
        }
        logger.info("Finished dealing with {} event", event);

        return observableProcess;
    }

    public ObservableProcess getProcessStatus(int processId) {

        //This is a concurrent map.
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(new ReentrantLock());
        ConcurrencyHelper oldValue = concurrencyHelperMap.putIfAbsent(processId, concurrencyHelper);
        if(oldValue != null) {
            concurrencyHelper = oldValue;
        }

        concurrencyHelper.getLock().lock();

        //If the process has been completed, return;
        ObservableProcess observableProcess = observableProcessStorage.retrieveProcess(processId);

        //this variable is method and so thread local
        ObservableProcessStateEnum lastObservedState = observableProcess.getLastObservedState();
        try {
            //Check condition
            while ((observableProcess = observableProcessStorage.retrieveProcess(processId)).getState().equals(lastObservedState)) {
                //Await on condition
                boolean await = concurrencyHelper.getCondition().await(properties.getTimeouts().get(observableProcess.getState()), TimeUnit.SECONDS);
                if(!await) {
                    logger.info("Request timeout!");
                    break;
                }
            }
            observableProcess.observe();
            observableProcessStorage.saveProcess(observableProcess);
        } catch (InterruptedException e) {
            logger.warn(e.getMessage());
        } finally {
            concurrencyHelper.getLock().unlock();
        }

        return observableProcess;

    }

    public enum EventTypeEnum {
        CREATE, PUT_DOCUMENT_COMPLETED, WAITED_COMPLETED
    }

    public static class ConcurrencyHelper {

        private Lock lock;
        private Condition condition;

        public ConcurrencyHelper() {}

        public ConcurrencyHelper(Lock lock) {
            this.lock = lock;
            this.condition = lock.newCondition();
        }

        public Lock getLock() {
            return lock;
        }

        public void setLock(Lock lock) {
            this.lock = lock;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }


}
