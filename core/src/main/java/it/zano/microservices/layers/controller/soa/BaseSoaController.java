package it.zano.microservices.layers.controller.soa;


/**
 * @author a.zanotti
 * @since 12/10/2018
 */
public abstract class BaseSoaController {

    protected final ExecutorDispatcher executorDispatcher;

    protected BaseSoaController(ExecutorDispatcher executorDispatcher){
        this.executorDispatcher = executorDispatcher;
    }

}
