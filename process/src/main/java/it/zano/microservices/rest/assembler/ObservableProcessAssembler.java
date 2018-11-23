package it.zano.microservices.rest.assembler;

import it.zano.microservices.controller.rest.BaseAssembler;
import it.zano.microservices.observableprocess.OprocImpl;
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
public class ObservableProcessAssembler extends BaseAssembler<OprocImpl, ObservableProcessResource> {


    @Autowired
    public ObservableProcessAssembler(Mapper mapper) {
        super(ObservableProcessController.class, ObservableProcessResource.class, mapper);
    }

    @Override
    public OprocImpl toModelClass(ObservableProcessResource resource) {
        OprocImpl observableProcess = new OprocImpl();
        mapper.map(resource,observableProcess);
        return observableProcess;
    }

    @Override
    public ObservableProcessResource toResource(OprocImpl observableProcess) {
        ObservableProcessResource observableProcessResource = createResourceWithId(observableProcess.getId(),observableProcess);
        mapper.map(observableProcess,observableProcessResource);
        observableProcessResource.setProcessId(observableProcess.getId());
        return observableProcessResource;
    }
}
