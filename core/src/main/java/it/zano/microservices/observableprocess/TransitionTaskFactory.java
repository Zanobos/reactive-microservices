package it.zano.microservices.observableprocess;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public abstract class TransitionTaskFactory<TRANSITION> {

    private final TransitionNotifier transitionNotifier;

    public TransitionTaskFactory(TransitionNotifier transitionNotifier) {
        this.transitionNotifier = transitionNotifier;
    }

    public abstract BaseTransitionTask createTask(TRANSITION transition);
}
