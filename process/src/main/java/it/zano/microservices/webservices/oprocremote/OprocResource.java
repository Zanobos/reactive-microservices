package it.zano.microservices.webservices.oprocremote;

import it.zano.microservices.observableprocess.OprocStateEnum;
import it.zano.microservices.webservices.rest.ArchRestResource;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public class OprocResource extends ArchRestResource {

    protected Integer id;
    protected OprocStateEnum actualState;
    protected OprocStateEnum lastObservedState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OprocStateEnum getActualState() {
        return actualState;
    }

    public void setActualState(OprocStateEnum actualState) {
        this.actualState = actualState;
    }

    public OprocStateEnum getLastObservedState() {
        return lastObservedState;
    }

    public void setLastObservedState(OprocStateEnum lastObservedState) {
        this.lastObservedState = lastObservedState;
    }
}
