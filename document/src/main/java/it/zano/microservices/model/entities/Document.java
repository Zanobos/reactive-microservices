package it.zano.microservices.model.entities;

import it.zano.microservices.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
@Entity
@Table(name = "documents")
public class Document extends BaseEntity {

    @Column(name = "document_title")
    private String documentTitle;

    @Column(name = "signature_expected")
    private String signatureExpected;

    @Column(name = "signature_actual")
    private String signatureActual;

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getSignatureExpected() {
        return signatureExpected;
    }

    public void setSignatureExpected(String signatureExpected) {
        this.signatureExpected = signatureExpected;
    }

    public String getSignatureActual() {
        return signatureActual;
    }

    public void setSignatureActual(String signatureActual) {
        this.signatureActual = signatureActual;
    }
}
