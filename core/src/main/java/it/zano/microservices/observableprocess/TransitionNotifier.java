package it.zano.microservices.observableprocess;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public interface TransitionNotifier<TRANSITION, IDTYPE> {

    void notifyTransition(TRANSITION transition, IDTYPE id);

}
