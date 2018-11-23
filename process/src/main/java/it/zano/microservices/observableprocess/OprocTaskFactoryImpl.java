package it.zano.microservices.observableprocess;

import it.zano.microservices.observableprocess.tasks.OprocBaseTask;
import it.zano.microservices.observableprocess.tasks.OprocCreateTask;
import it.zano.microservices.observableprocess.tasks.OprocPutDocumentCompletedTask;
import it.zano.microservices.observableprocess.tasks.OprocWaitedCompletedTask;
import it.zano.microservices.webservices.documents.DocumentTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
@Service
public class OprocTaskFactoryImpl extends TransitionTaskFactory<OprocTransitionEnum, Integer, OprocBaseTask> {

    private DocumentTemplate documentTemplate;

    @Autowired
    public OprocTaskFactoryImpl(TransitionNotifier<OprocTransitionEnum, Integer> transitionNotifier,
                                DocumentTemplate documentTemplate) {
        super(transitionNotifier);
        this.documentTemplate = documentTemplate;
    }

    @Override
    public OprocBaseTask createTask(OprocTransitionEnum oprocTransitionEnum, Integer id) {

        OprocBaseTask oprocBaseTask;
        switch (oprocTransitionEnum) {
            case CREATE: {
                oprocBaseTask = new OprocCreateTask(transitionNotifier, oprocTransitionEnum, id, documentTemplate);
                break;
            }
            case PUT_DOCUMENT_COMPLETED: {
                oprocBaseTask = new OprocPutDocumentCompletedTask(transitionNotifier, oprocTransitionEnum, id);
                break;
            }
            case WAITED_COMPLETED: {
                oprocBaseTask = new OprocWaitedCompletedTask(transitionNotifier, oprocTransitionEnum, id);
                break;
            }
            default: {
                oprocBaseTask = null;
                break;
            }
        }
        return oprocBaseTask;
    }
}
