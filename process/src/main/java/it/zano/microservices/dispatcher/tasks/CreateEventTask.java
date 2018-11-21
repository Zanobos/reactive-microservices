package it.zano.microservices.dispatcher.tasks;

import it.zano.microservices.dispatcher.ObservableProcessManager;
import it.zano.microservices.event.observableprocess.EventTaskMessage;
import it.zano.microservices.event.observableprocess.ObservableProcessEventPublisher;
import it.zano.microservices.webservices.documents.DocumentResource;
import it.zano.microservices.webservices.documents.DocumentTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public class CreateEventTask extends BaseTask {

    private static final String URI_ID = "id";

    private DocumentTemplate documentTemplate;
    private Integer id;
    private ObservableProcessEventPublisher eventPublisher;

    public CreateEventTask(DocumentTemplate documentTemplate, Integer id, ObservableProcessEventPublisher eventPublisher) {
        this.documentTemplate = documentTemplate;
        this.id = id;
        this.eventPublisher = eventPublisher;
    }

    @Override
    protected void doRun() {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put(URI_ID, "" + id);
        DocumentResource document = new DocumentResource();
        document.setDocumentTitle("ObservableTry");
        document.setSignature("ObservableSignature");
        documentTemplate.put(documentTemplate.getEndpoint() + "{" + URI_ID + "}", document, uriVariables);
        EventTaskMessage message = new EventTaskMessage();
        message.setProcessId(id);
        message.setEventTypeEnum(ObservableProcessManager.EventTypeEnum.PUT_DOCUMENT_COMPLETED);
        eventPublisher.sendMessage(message);
    }
}
