package it.zano.microservices.observableprocess;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public abstract class ObservableProcess<STATE, IDTYPE> {

    protected IDTYPE id;
    protected STATE actualState;
    protected STATE lastObservedState;

    public void observe() {
        lastObservedState = actualState;
    }

    public void setActualState(STATE state) {
        actualState = state;
    }

    public STATE getLastObservedState() {
        return lastObservedState;
    }

    public STATE getActualState() {
        return actualState;
    }

    public IDTYPE getId() {
        return id;
    }

    public void setId(IDTYPE id) {
        this.id = id;
    }

    public void setLastObservedState(STATE lastObservedState) {
        this.lastObservedState = lastObservedState;
    }

    public void init(IDTYPE id, STATE initialState) {
        this.id = id;
        this.actualState = initialState;
        this.lastObservedState = initialState;
    }
}
