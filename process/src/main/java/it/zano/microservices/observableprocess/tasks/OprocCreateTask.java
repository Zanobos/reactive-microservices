package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.EventNotifier;
import it.zano.microservices.observableprocess.OprocEventEnum;
import it.zano.microservices.observableprocess.OprocEventMessage;
import it.zano.microservices.observableprocess.OprocImpl;
import it.zano.microservices.webservices.documents.DocumentResource;
import it.zano.microservices.webservices.documents.DocumentTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public class OprocCreateTask extends OprocBaseTask {

    private static final String URI_ID = "id";

    private DocumentTemplate documentTemplate;

    public OprocCreateTask(EventNotifier<OprocEventMessage> eventNotifier,
                           OprocEventEnum oprocEventEnum,
                           OprocImpl process,
                           DocumentTemplate documentTemplate) {
        super(eventNotifier, oprocEventEnum, process);
        this.documentTemplate = documentTemplate;
    }

    @Override
    protected void execute() {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put(URI_ID, "" + process.getId());
        DocumentResource document = new DocumentResource();
        document.setDocumentTitle("ObservableTry");
        document.setSignature("ObservableSignature");
        documentTemplate.put(documentTemplate.getEndpoint() + "{" + URI_ID + "}", document, uriVariables);
        OprocEventMessage message = new OprocEventMessage(OprocEventEnum.PUT_DOCUMENT_COMPLETED,process.getId());
        eventNotifier.notifyEvent(message);
    }
}
