package it.zano.microservices.observableprocess;

import it.zano.microservices.observableprocess.tasks.*;
import it.zano.microservices.webservices.documents.DocumentTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
@Service
public class OprocTaskFactoryImpl extends TransitionTaskFactory<OprocTransitionEnum, OprocStateEnum, Integer, OprocTransitionMessage,
        OprocImpl, OprocBaseTask> {

    private static final Logger logger = LoggerFactory.getLogger(OprocTaskFactoryImpl.class);

    private DocumentTemplate documentTemplate;

    @Autowired
    public OprocTaskFactoryImpl(TransitionNotifier<OprocTransitionMessage> transitionNotifier,
                                DocumentTemplate documentTemplate) {
        super(transitionNotifier);
        this.documentTemplate = documentTemplate;
    }

    @Override
    public OprocBaseTask createTask(OprocTransitionEnum oprocTransitionEnum, OprocImpl oprocImpl, Object... args) {

        OprocBaseTask oprocBaseTask;
        switch (oprocTransitionEnum) {
            case CREATE: {
                oprocBaseTask = new OprocCreateTask(transitionNotifier, oprocTransitionEnum, oprocImpl, documentTemplate);
                break;
            }
            case PUT_DOCUMENT_COMPLETED: {
                oprocBaseTask = new OprocPutDocumentCompletedTask(transitionNotifier, oprocTransitionEnum, oprocImpl);
                break;
            }
            case WAITED_COMPLETED: {
                oprocBaseTask = new OprocWaitedCompletedTask(transitionNotifier,oprocTransitionEnum,oprocImpl);
                break;
            }
            case AGAIN_WAITING_COMPLETED: {
                oprocBaseTask = new OprocWaitedAgainCompletedTask(transitionNotifier, oprocTransitionEnum, oprocImpl);
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
