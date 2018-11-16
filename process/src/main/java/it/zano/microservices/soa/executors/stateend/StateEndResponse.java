package it.zano.microservices.soa.executors.stateend;

import it.zano.microservices.controller.soa.BaseSoaResponsePayload;

/**
 * @author a.zanotti
 * @since 16/10/2018
 */
public class StateEndResponse extends BaseSoaResponsePayload {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
