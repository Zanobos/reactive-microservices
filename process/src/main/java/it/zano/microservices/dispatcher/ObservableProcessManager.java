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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Service
public class ObservableProcessManager {

    private final static Logger logger = LoggerFactory.getLogger(ObservableProcessManager.class);

    private DocumentTemplate documentTemplate;
    private ObservableProcessStorage observableProcessStorage;
    private ExecutorService executorService;
    private ObservableProcessEventPublisher eventPublisher;

    @Autowired
    public ObservableProcessManager(DocumentTemplate documentTemplate,
                                    ObservableProcessStorage observableProcessStorage,
                                    ObservableProcessEventPublisher eventPublisher) {
        this.documentTemplate = documentTemplate;
        this.observableProcessStorage = observableProcessStorage;
        this.eventPublisher = eventPublisher;
        this.executorService = Executors.newWorkStealingPool();
    }

    public ObservableProcess executeEvent(EventTypeEnum event, Integer processId) {

        ObservableProcess observableProcess;
        logger.info("Start dealing with {} event", event);
        switch (event) {
            case CREATE:
                processId = 1024;
                observableProcess = new ObservableProcess();
                observableProcess.setId(processId);
                observableProcess.setState(ObservableProcessStateEnum.PUT_DOCUMENT_IN_PROGRESS);
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
        logger.info("Finished dealing with {} event", event);

        return observableProcess;
    }

    public ObservableProcess getProcessStatus(int processid) {
        //Here return directly or wait for:
        //1. a change of status
        //2. a timeout
        ObservableProcess observableProcess = observableProcessStorage.retrieveProcess(processid);
        return observableProcess;
    }

    public enum EventTypeEnum {
        CREATE, PUT_DOCUMENT_COMPLETED, WAITED_COMPLETED
    }


}
