package it.zano.microservices.event;

import it.zano.microservices.controller.event.ErrorMessage;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public class ErrorInSignMessage extends ErrorMessage {

    private String documentId;

    public ErrorInSignMessage() {
    }

    public ErrorInSignMessage(String serviceId, String errorId, String documentId) {
        super(serviceId, errorId);
        this.documentId = documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
