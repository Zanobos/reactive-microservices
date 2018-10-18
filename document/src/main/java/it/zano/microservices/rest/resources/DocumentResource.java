package it.zano.microservices.rest.resources;

import it.zano.microservices.controller.rest.BaseResource;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
public class DocumentResource extends BaseResource {

    private String documentTitle;
    private String signature;

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
