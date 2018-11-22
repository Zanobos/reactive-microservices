package it.zano.microservices.dispatcher;

import it.zano.microservices.dispatcher.tasks.CreateEventTask;
import it.zano.microservices.dispatcher.tasks.PutDocumentCompletedEventTask;
import it.zano.microservices.event.observableprocess.ObservableProcessEventPublisher;
import it.zano.microservices.model.beans.ObservableProcess;
import it.zano.microservices.model.beans.ObservableProcessStateEnum;
import it.zano.microservices.model.beans.ObservableProcessTransitionEnum;
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

    public ObservableProcess executeEvent(ObservableProcessTransitionEnum event, Integer processId) {

        //Add a new condition variable for this process
        ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper(new ReentrantLock());
        ConcurrencyHelper oldValue = concurrencyHelperMap.putIfAbsent(processId, concurrencyHelper);
        if(oldValue != null)
            concurrencyHelper = oldValue;

        //TODO this lock must be shared between threads (and applications...)
        concurrencyHelper.getLock().lock();
        //Starting point
        ObservableProcess observableProcess = observableProcessStorage.retrieveProcess(processId);
        //In any case I create one process
        if(observableProcess == null) {
            observableProcess = new ObservableProcess();
            observableProcess.setId(processId);
            observableProcess.setState(ObservableProcessStateEnum.NOT_OBSERVED);
            observableProcess.setLastObservedState(ObservableProcessStateEnum.NOT_OBSERVED);
        }

        try {
            //Proceed if the starting state and the transition asked are ok
            ObservableProcessStateEnum startingState = properties.getTransitions().get(event).getFrom();
            //If the event arrives to two different applications/thread, one of that has the lock, so it will stop it
            if(startingState == observableProcess.getState()) {
                logger.info("Start dealing with {} event", event);
                switch (event) {
                    case CREATE:
                        //Async call -> this call an external service
                        executorService.execute(new CreateEventTask(documentTemplate, processId, eventPublisher));
                        observableProcess.setState(ObservableProcessStateEnum.PUT_DOCUMENT_IN_PROGRESS);
                        break;
                    case PUT_DOCUMENT_COMPLETED:
                        //Async call -> this just perform local stuff
                        executorService.execute(new PutDocumentCompletedEventTask(processId, eventPublisher));
                        observableProcess.setState(ObservableProcessStateEnum.WAITED_IN_PROGRESS);
                        break;
                    case WAITED_COMPLETED:
                        //No call to service, just end the process
                        observableProcess.setState(ObservableProcessStateEnum.END);
                        break;
                    default:
                        //it should never happen
                        observableProcess = null;
                }
                observableProcessStorage.saveProcess(observableProcess);
                concurrencyHelper.getChangedStatusCondition().signalAll();
            } else {
                logger.warn("Event {} discarded because actual state is {} while expected state is {}",
                        event, observableProcess.getState(), startingState);
            }
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
                Integer timeout = properties.getStates().get(observableProcess.getState()).getTimeout();
                //Await on condition
                boolean await = concurrencyHelper.getChangedStatusCondition().await(timeout, TimeUnit.SECONDS);
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

    public static class ConcurrencyHelper {

        private Lock lock;
        private Condition changedStatusCondition;
        private Condition workDoneCondition;

        public ConcurrencyHelper() {}

        public ConcurrencyHelper(Lock lock) {
            this.lock = lock;
            this.changedStatusCondition = lock.newCondition();
            this.workDoneCondition = lock.newCondition();
        }

        public Lock getLock() {
            return lock;
        }

        public Condition getChangedStatusCondition() {
            return changedStatusCondition;
        }

        public Condition getWorkDoneCondition() {
            return workDoneCondition;
        }
    }


}
