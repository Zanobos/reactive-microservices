package it.zano.microservices.soa.executors;

import it.zano.microservices.controller.soa.BaseSoaLogicExecutor;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.webservices.person.PersonClient;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 07/11/2018
 */
@Service
public class StateCheckExecutorBaseSoa extends BaseSoaLogicExecutor<StateCheckRequest, StateCheckResponse,
        StateCheckRequest, StateCheckResponse> {

    private PersonClient personClient;

    @Autowired
    public StateCheckExecutorBaseSoa(Mapper mapper, PersonClient personClient) {
        super(mapper);
        this.personClient = personClient;
    }

    @Override
    protected StateCheckResponse performBusinessLogic(StateCheckRequest innerRequest) throws MicroServiceException {
        Boolean isBlocked = personClient.checkPerson(innerRequest.getFirstName(), innerRequest.getLastName());
        StateCheckResponse response = new StateCheckResponse();
        response.setBlocked(isBlocked);
        return response;
    }
}
