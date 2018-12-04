package it.zano.microservices.observableprocess;

import it.zano.microservices.observableprocess.tasks.OprocBaseTask;
import it.zano.microservices.observableprocess.tasks.OprocClearTask;
import it.zano.microservices.observableprocess.tasks.OprocCreateTask;
import it.zano.microservices.observableprocess.tasks.OprocPutDocumentCompletedTask;
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
public class OprocTaskFactoryImpl extends TaskFactory<OprocEventEnum, OprocStateEnum, Integer, OprocEventMessage,
        OprocImpl, OprocBaseTask> {

    private static final Logger logger = LoggerFactory.getLogger(OprocTaskFactoryImpl.class);

    private DocumentTemplate documentTemplate;

    @Autowired
    public OprocTaskFactoryImpl(EventNotifier<OprocEventMessage> eventNotifier,
                                DocumentTemplate documentTemplate) {
        super(eventNotifier);
        this.documentTemplate = documentTemplate;
    }

    @Override
    public OprocBaseTask createTask(OprocEventEnum oprocEventEnum, OprocImpl oprocImpl, Object... args) {

        OprocBaseTask oprocBaseTask;
        switch (oprocEventEnum) {
            case CREATE: {
                oprocBaseTask = new OprocCreateTask(eventNotifier, oprocEventEnum, oprocImpl, documentTemplate);
                break;
            }
            case PUT_DOCUMENT_COMPLETED: {
                oprocBaseTask = new OprocPutDocumentCompletedTask(eventNotifier, oprocEventEnum, oprocImpl);
                break;
            }
            case AGAIN_WAITING_COMPLETED: {
                oprocBaseTask = new OprocClearTask(eventNotifier, oprocEventEnum, oprocImpl);
                break;
            }
            case WAITED_COMPLETED:
            default: {
                oprocBaseTask = null;
                break;
            }
        }
        return oprocBaseTask;
    }
}
