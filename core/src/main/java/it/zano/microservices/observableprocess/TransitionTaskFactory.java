package it.zano.microservices.observableprocess;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public abstract class TransitionTaskFactory<TRANSITION, STATE, IDTYPE, MESSAGE,
        OPROC extends ObservableProcess<STATE, IDTYPE>,
        TASK extends BaseTransitionTask<TRANSITION, STATE, IDTYPE, MESSAGE, OPROC>> {

    protected final TransitionNotifier<MESSAGE> transitionNotifier;

    public TransitionTaskFactory(TransitionNotifier<MESSAGE> transitionNotifier) {
        this.transitionNotifier = transitionNotifier;
    }

    public abstract TASK createTask(TRANSITION transition, OPROC process, Object... args);
}
