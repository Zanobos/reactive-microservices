package it.zano.microservices.observableprocess;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public interface ObservableProcess<STATE> {
    void observe();
    void setActualState(STATE state);
    STATE getLastObservedState();
    STATE getActualState();

}
