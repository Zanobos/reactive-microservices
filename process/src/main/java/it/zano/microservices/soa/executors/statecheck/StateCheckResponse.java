package it.zano.microservices.soa.executors.statecheck;

import it.zano.microservices.controller.soa.BaseSoaResponsePayload;

/**
 * @author a.zanotti
 * @since 07/11/2018
 */
public class StateCheckResponse extends BaseSoaResponsePayload {

    private boolean blocked;

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
