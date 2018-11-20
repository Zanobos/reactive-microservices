package it.zano.microservices.rest.controllers;

import io.swagger.annotations.Api;
import it.zano.microservices.controller.rest.BaseRestController;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.model.entities.ProcessInfo;
import it.zano.microservices.model.repositories.ProcessRepository;
import it.zano.microservices.rest.assembler.ProcessAssembler;
import it.zano.microservices.rest.resources.ProcessResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author a.zanotti
 * @since 12/10/2018
 */
@Api(tags = "processes")
@RestController
@RequestMapping(value = "/processes", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProcessesController extends BaseRestController<ProcessInfo,ProcessResource> {

    @Autowired
    protected ProcessesController(ProcessAssembler assembler, ProcessRepository processRepository) {
        super(assembler);
        this.processRepository = processRepository;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProcessResource> getProcess(@RequestHeader HttpHeaders httpHeaders, @PathVariable(value = "id") Integer id) throws MicroServiceException {
        ProcessInfo process = processRepository.findById(id).orElseThrow(() -> new MicroServiceException("Not found"));
        ProcessResource userResource = assembler.toResource(process);
        return ResponseEntity.ok(userResource);
    }

    private final ProcessRepository processRepository;
}
