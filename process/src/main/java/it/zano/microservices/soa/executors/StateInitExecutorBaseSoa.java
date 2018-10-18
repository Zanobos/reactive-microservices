package it.zano.microservices.soa.executors;

import it.zano.microservices.controller.soa.BaseSoaLogicExecutor;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 16/10/2018
 */
@Service
public class StateInitExecutorBaseSoa extends BaseSoaLogicExecutor<StateInitRequest, StateInitResponse,
        StateInitRequest, StateInitResponse> {

    @Autowired
    public StateInitExecutorBaseSoa(Mapper mapper) {
        super(mapper);
    }

    @Override
    protected StateInitResponse performBusinessLogic(StateInitRequest innerRequest) {
        StateInitResponse stateInitResponse = new StateInitResponse();
        stateInitResponse.setStatus("OK");
        return stateInitResponse;

    }
}
