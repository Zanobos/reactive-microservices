package it.zano.microservices.soa;

import it.zano.microservices.layers.controller.soa.BaseSoaLogicExecutor;

/**
 * @author a.zanotti
 * @since 16/10/2018
 */
public class StateInitExecutorBaseSoa extends BaseSoaLogicExecutor<StateInitRequest, StateInitResponse,
        StateInitRequest, StateInitResponse> {


    @Override
    protected StateInitResponse performBusinessLogic(StateInitRequest innerRequest) {
        StateInitResponse stateInitResponse = new StateInitResponse();
        stateInitResponse.setStatus("OK");
        return stateInitResponse;

    }
}
