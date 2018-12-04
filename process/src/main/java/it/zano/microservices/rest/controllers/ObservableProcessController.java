package it.zano.microservices.rest.controllers;

import io.swagger.annotations.Api;
import it.zano.microservices.controller.rest.BaseRestController;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.exception.ObservableProcessNotFoundException;
import it.zano.microservices.observableprocess.OprocImpl;
import it.zano.microservices.observableprocess.OprocManagerImpl;
import it.zano.microservices.rest.assembler.ObservableProcessAssembler;
import it.zano.microservices.rest.resources.ObservableProcessResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static it.zano.microservices.observableprocess.OprocEventEnum.CREATE;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Api(tags = "oprocs")
@RestController
@RequestMapping(value = "/oprocs", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ObservableProcessController extends BaseRestController<OprocImpl, ObservableProcessResource>{

    private OprocManagerImpl processManager;

    @Autowired
    protected ObservableProcessController(ObservableProcessAssembler assembler, OprocManagerImpl processManager) {
        super(assembler);
        this.processManager = processManager;
    }

    @PostMapping
    public ResponseEntity<ObservableProcessResource> createObservableProcess(@RequestHeader HttpHeaders httpHeaders,
                                                                             @RequestBody ObservableProcessResource resource) {
        OprocImpl oproc = processManager.executeEvent(CREATE, resource.getProcessId());
        ObservableProcessResource observableProcessResource = assembler.toResource(oproc);
        return ResponseEntity.ok(observableProcessResource);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ObservableProcessResource> getObservableProcess(@RequestHeader HttpHeaders httpHeaders,
                                                                          @PathVariable(value = "id") Integer id) throws MicroServiceException {
        OprocImpl oprocImpl = processManager.getObservableProcessStatus(id);
        if(oprocImpl == null) {
            throw new ObservableProcessNotFoundException(id);
        }
        ObservableProcessResource observableProcessResource = assembler.toResource(oprocImpl);
        return ResponseEntity.ok(observableProcessResource);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteObservableProcess(@RequestHeader HttpHeaders httpHeaders,
                                                        @PathVariable(value = "id") Integer id) throws MicroServiceException {
        processManager.clearResources(id);
        return ResponseEntity.ok().build();
    }

}
