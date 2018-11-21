package it.zano.microservices.model.beans;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public class ObservableProcess {

    private Integer id;
    private ObservableProcessStateEnum state;
    private ObservableProcessStateEnum lastObservedState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ObservableProcessStateEnum getState() {
        return state;
    }

    public void setState(ObservableProcessStateEnum state) {
        this.state = state;
    }

    public ObservableProcessStateEnum getLastObservedState() {
        return lastObservedState;
    }

    public void setLastObservedState(ObservableProcessStateEnum lastObservedState) {
        this.lastObservedState = lastObservedState;
    }

    public void observe() {
        this.lastObservedState = state;
    }
}
