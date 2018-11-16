package it.zano.microservices.soa.executors.stateend;

import it.zano.microservices.controller.soa.BaseSoaLogicExecutor;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 16/10/2018
 */
@Service
public class StateEndExecutor extends BaseSoaLogicExecutor<StateEndRequest, StateEndResponse,
        StateEndRequest, StateEndResponse> {

    @Autowired
    public StateEndExecutor(Mapper mapper) {
        super(mapper);
    }

    @Override
    protected StateEndResponse performBusinessLogic(StateEndRequest innerRequest) {
        StateEndResponse stateEndResponse = new StateEndResponse();
        stateEndResponse.setStatus("OK");
        return stateEndResponse;

    }
}
