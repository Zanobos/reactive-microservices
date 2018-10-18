package it.zano.microservices.rest.assembler;

import it.zano.microservices.rest.controllers.DocumentsController;
import it.zano.microservices.controller.rest.BaseAssembler;
import it.zano.microservices.model.entities.Document;
import it.zano.microservices.rest.resources.DocumentResource;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
@Service
public class DocumentsAssembler extends BaseAssembler<Document,DocumentResource> {

    @Autowired
    public DocumentsAssembler(Mapper mapper) {
        super(DocumentsController.class, DocumentResource.class, mapper);
    }

    @Override
    public DocumentResource toResource(Document document) {
        DocumentResource documentResource = createResourceWithId(document.getId(),document);
        mapper.map(document, documentResource);
        //Custom mapping
        documentResource.setSignature(document.getSignatureActual());
        return documentResource;
    }

    @Override
    public Document toModelClass(DocumentResource resource) {
        Document document = new Document();
        mapper.map(resource,document);
        //custom mapping
        document.setSignatureExpected(resource.getSignature());
        return document;
    }
}
