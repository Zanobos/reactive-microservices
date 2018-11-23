package it.zano.microservices.rest.resources;

import it.zano.microservices.controller.rest.BaseResource;
import it.zano.microservices.observableprocess.OprocStateEnum;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public class ObservableProcessResource extends BaseResource {

    private Integer processId;
    private OprocStateEnum actualState;

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public OprocStateEnum getActualState() {
        return actualState;
    }

    public void setActualState(OprocStateEnum actualState) {
        this.actualState = actualState;
    }
}
