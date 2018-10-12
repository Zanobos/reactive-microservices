package it.zano.microservices.controllers.process;

import it.zano.microservices.layers.controller.rest.BaseRestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/processes", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ProcessesController extends BaseRestController {

    @GetMapping(value = "/{id}")
    public ResponseEntity<String> getProcess(@PathVariable(value = "id") String id){
        String payload = "prova" + id;
        return ResponseEntity.ok(payload);
    }

}
