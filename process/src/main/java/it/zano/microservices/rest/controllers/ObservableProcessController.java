package it.zano.microservices.rest.controllers;

import io.swagger.annotations.Api;
import it.zano.microservices.controller.rest.BaseRestController;
import it.zano.microservices.dispatcher.ObservableProcessManager;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.model.beans.ObservableProcess;
import it.zano.microservices.rest.assembler.ObservableProcessAssembler;
import it.zano.microservices.rest.resources.ObservableProcessResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Api(tags = "oproc")
@RestController
@RequestMapping(value = "/oproc", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ObservableProcessController extends BaseRestController<ObservableProcess, ObservableProcessResource>{

    private ObservableProcessManager processManager;

    @Autowired
    protected ObservableProcessController(ObservableProcessAssembler assembler, ObservableProcessManager processManager) {
        super(assembler);
        this.processManager = processManager;
    }

    @PostMapping
    public ResponseEntity<ObservableProcessResource> createObservableProcess(@RequestHeader HttpHeaders httpHeaders) {
        ObservableProcess observableProcess = processManager.executeEvent(ObservableProcessManager.EventTypeEnum.CREATE, null);
        ObservableProcessResource observableProcessResource = assembler.toResource(observableProcess);
        return ResponseEntity.ok(observableProcessResource);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ObservableProcessResource> getObservableProcess(@RequestHeader HttpHeaders httpHeaders,
                                                                          @PathVariable(value = "id") Integer id) throws MicroServiceException {
        ObservableProcess observableProcess = processManager.getProcessStatus(id);
        ObservableProcessResource observableProcessResource = assembler.toResource(observableProcess);
        return ResponseEntity.ok(observableProcessResource);
    }

}
