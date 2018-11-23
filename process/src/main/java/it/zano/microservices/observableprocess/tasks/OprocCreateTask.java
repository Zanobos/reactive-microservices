package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.OprocTransitionEnum;
import it.zano.microservices.observableprocess.TransitionNotifier;
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

    public OprocCreateTask(TransitionNotifier<OprocTransitionEnum, Integer> transitionNotifier,
                           OprocTransitionEnum oprocTransitionEnum, Integer id,
                           DocumentTemplate documentTemplate) {
        super(transitionNotifier, oprocTransitionEnum, id);
        this.documentTemplate = documentTemplate;
    }

    @Override
    protected void execute() {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put(URI_ID, "" + id.get());
        DocumentResource document = new DocumentResource();
        document.setDocumentTitle("ObservableTry");
        document.setSignature("ObservableSignature");
        documentTemplate.put(documentTemplate.getEndpoint() + "{" + URI_ID + "}", document, uriVariables);
    }
}
