package it.zano.microservices.event;

import java.io.Serializable;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public class SignDocMessage implements Serializable{

    private static final long serialVersionUID = 42L;

    private String userId;
    private String documentId;

    public SignDocMessage(String userId, String documentId) {
        this.userId = userId;
        this.documentId = documentId;
    }

    public SignDocMessage() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
