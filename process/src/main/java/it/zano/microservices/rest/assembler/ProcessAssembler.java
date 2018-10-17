package it.zano.microservices.rest.assembler;

import it.zano.microservices.rest.controllers.ProcessesController;
import it.zano.microservices.layers.controller.rest.BaseAssembler;
import it.zano.microservices.model.ProcessInfo;
import it.zano.microservices.rest.resources.ProcessResource;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
@Service
public class ProcessAssembler extends BaseAssembler<ProcessInfo,ProcessResource> {

    @Autowired
    public ProcessAssembler(Mapper mapper) {
        super(ProcessesController.class, ProcessResource.class, mapper);
    }

    @Override
    public ProcessResource toResource(ProcessInfo process) {
        ProcessResource processResource = createResourceWithId(process.getProcessCode(),process);
        mapper.map(process,processResource);
        return processResource;
    }
}
