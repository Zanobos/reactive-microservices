package it.zano.microservices.layers.controller.soa;

import java.util.HashMap;
import java.util.Map;

/**
 * @author a.zanotti
 * @since 16/10/2018
 */
public abstract class ExecutorDispatcher {

    private Map<Class, LogicExecutor<? extends BaseSoaRequestPayload, ? extends BaseSoaResponsePayload>> executorMap;

    protected ExecutorDispatcher() {
        this.executorMap = new HashMap<>();
    }

    public LogicExecutor<? extends BaseSoaRequestPayload, ? extends BaseSoaResponsePayload> getLogicExecutor(Class exClass) {
        return executorMap.get(exClass);
    }

}
