package it.zano.microservices.rest.resources;

import it.zano.microservices.layers.controller.rest.BaseResource;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
public class ProcessResource extends BaseResource {

    private String processCode;
    private String processState;

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }
}
