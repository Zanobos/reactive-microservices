package it.zano.microservices.observableprocess;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public interface TransitionNotifier<TRANSITION, IDTYPE> {

    void notifyTransitionCompleted(TRANSITION transition, IDTYPE id);

}
