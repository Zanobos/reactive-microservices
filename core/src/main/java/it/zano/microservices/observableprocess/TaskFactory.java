package it.zano.microservices.observableprocess;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public abstract class TaskFactory<TRANSITION, STATE, IDTYPE, MESSAGE,
        OPROC extends ObservableProcess<STATE, IDTYPE>,
        TASK extends BaseTask<TRANSITION, STATE, IDTYPE, MESSAGE, OPROC>> {

    protected final EventNotifier<MESSAGE> eventNotifier;

    public TaskFactory(EventNotifier<MESSAGE> eventNotifier) {
        this.eventNotifier = eventNotifier;
    }

    public abstract TASK createTask(TRANSITION transition, OPROC process, Object... args);
}
