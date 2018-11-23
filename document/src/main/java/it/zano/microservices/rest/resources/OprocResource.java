package it.zano.microservices.rest.resources;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public class OprocResource {

    private Integer processId;
    private String actualState;
    private String lastObservedState;

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public String getActualState() {
        return actualState;
    }

    public void setActualState(String actualState) {
        this.actualState = actualState;
    }

    public String getLastObservedState() {
        return lastObservedState;
    }

    public void setLastObservedState(String lastObservedState) {
        this.lastObservedState = lastObservedState;
    }
}
