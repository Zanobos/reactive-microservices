package it.zano.microservices.rest.controllers;

import io.swagger.annotations.Api;
import it.zano.microservices.layers.controller.rest.BaseAssembler;
import it.zano.microservices.layers.controller.rest.BaseRestController;
import it.zano.microservices.model.ProcessInfo;
import it.zano.microservices.rest.resources.ProcessResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author a.zanotti
 * @since 12/10/2018
 */
@Api(tags = "processes")
@RestController
@RequestMapping(value = "/processes", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProcessesController extends BaseRestController<ProcessInfo,ProcessResource> {

    @Autowired
    protected ProcessesController(BaseAssembler<ProcessInfo,ProcessResource> assembler) {
        super(assembler);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProcessResource> getProcess(@PathVariable(value = "id") String id){
        ProcessInfo process = new ProcessInfo();
        process.setProcessCode("CA");
        process.setProcessState("START");
        ProcessResource userResource = assembler.toResource(process);
        return ResponseEntity.ok(userResource);
    }

}
