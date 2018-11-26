package it.zano.microservices.observableprocess;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public interface TransitionNotifier<MESSAGE> {

    void notifyTransitionCompleted(MESSAGE message);

}
