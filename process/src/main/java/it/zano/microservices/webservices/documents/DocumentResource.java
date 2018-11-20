package it.zano.microservices.webservices.documents;

import it.zano.microservices.webservices.rest.ArchRestResource;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public class DocumentResource extends ArchRestResource {

    private Integer id;
    private String documentTitle;
    private String signature;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
