package it.zano.microservices.observableprocess;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public abstract class TransitionTaskFactory<TRANSITION, STATE, IDTYPE,
         OPROC extends ObservableProcess<STATE, IDTYPE>, TASK extends BaseTransitionTask<TRANSITION, IDTYPE>> {

    protected final TransitionNotifier<TRANSITION, IDTYPE> transitionNotifier;

    public TransitionTaskFactory(TransitionNotifier<TRANSITION, IDTYPE> transitionNotifier) {
        this.transitionNotifier = transitionNotifier;
    }

    public abstract TASK createTask(TRANSITION transition, OPROC process, Object... args);
}
