package it.zano.microservices.rest.assembler;

import it.zano.microservices.controller.rest.BaseAssembler;
import it.zano.microservices.model.beans.ObservableProcess;
import it.zano.microservices.rest.controllers.ObservableProcessController;
import it.zano.microservices.rest.resources.ObservableProcessResource;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Service
public class ObservableProcessAssembler extends BaseAssembler<ObservableProcess, ObservableProcessResource> {


    @Autowired
    public ObservableProcessAssembler(Mapper mapper) {
        super(ObservableProcessController.class, ObservableProcessResource.class, mapper);
    }

    @Override
    public ObservableProcess toModelClass(ObservableProcessResource resource) {
        ObservableProcess observableProcess = new ObservableProcess();
        mapper.map(resource,observableProcess);
        return observableProcess;
    }

    @Override
    public ObservableProcessResource toResource(ObservableProcess observableProcess) {
        ObservableProcessResource observableProcessResource = createResourceWithId(observableProcess.getId(),observableProcess);
        mapper.map(observableProcess,observableProcessResource);
        return observableProcessResource;
    }
}
