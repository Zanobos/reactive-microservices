package it.zano.microservices.rest.controllers;

import io.swagger.annotations.Api;
import it.zano.microservices.controller.rest.BaseAssembler;
import it.zano.microservices.controller.rest.BaseRestController;
import it.zano.microservices.event.DocumentsEventController;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.model.entities.Document;
import it.zano.microservices.model.repositories.DocumentRepository;
import it.zano.microservices.rest.resources.DocumentResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author a.zanotti
 * @since 12/10/2018
 */
@Api(tags = "documents")
@RestController
@RequestMapping(value = "/documents", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DocumentsController extends BaseRestController<Document,DocumentResource> {

    @Autowired
    protected DocumentsController(BaseAssembler<Document,DocumentResource> assembler,
                                  DocumentRepository documentRepository,
                                  DocumentsEventController rabbitController) {
        super(assembler);
        this.documentRepository = documentRepository;
        this.rabbitController = rabbitController;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DocumentResource> getDocument(@RequestHeader HttpHeaders httpHeaders, @PathVariable(value = "id") Integer id) throws MicroServiceException {
        Document document = documentRepository.findById(id).orElseThrow(() -> new MicroServiceException("Not found"));
        DocumentResource userResource = assembler.toResource(document);
        return ResponseEntity.ok(userResource);
    }

    @GetMapping
    public ResponseEntity<List<DocumentResource>> getDocuments(@RequestHeader HttpHeaders httpHeaders) {
        List<Document> documents = documentRepository.findAll();
        List<DocumentResource> documentResources = assembler.toResources(documents);
        return ResponseEntity.ok(documentResources);
    }

    //Sign the document only if signature actual == signature expected
    @PatchMapping(value = "/{id}")
    public ResponseEntity<DocumentResource> patchDocument(@RequestHeader HttpHeaders httpHeaders,
                                                          @PathVariable(value = "id") Integer id,
                                                          @RequestBody DocumentResource documentResource) throws MicroServiceException {
        Document document = documentRepository.findById(id).orElseThrow(() -> new MicroServiceException("Not found"));
        String signature = documentResource.getSignature();
        if(!signature.equals(document.getSignatureExpected())) {
            rabbitController.errorInSign(""+id);
            throw new MicroServiceException("Signature invalid");
        }
        document.setSignatureActual(signature);
        Document documentSaved = documentRepository.save(document);
        DocumentResource documentResourceOutput = assembler.toResource(documentSaved);
        return ResponseEntity.ok(documentResourceOutput);
    }

    @PostMapping
    public ResponseEntity<DocumentResource> postDocument(@RequestHeader HttpHeaders httpHeaders, @RequestBody DocumentResource documentResource) {
        Document document = assembler.toModelClass(documentResource);
        Document documentSaved = documentRepository.save(document);
        DocumentResource resource = assembler.toResource(documentSaved);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<DocumentResource> deleteDocument(@RequestHeader HttpHeaders httpHeaders, @PathVariable(value = "id") Integer id) throws MicroServiceException {
        Document document = documentRepository.findById(id).orElseThrow(() -> new MicroServiceException("Not found"));
        documentRepository.delete(document);
        DocumentResource resource = assembler.toResource(document);
        return ResponseEntity.ok(resource);
    }


    private final DocumentRepository documentRepository;
    private final DocumentsEventController rabbitController;
}
