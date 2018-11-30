package it.zano.microservices.observableprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * True command pattern
 * @author a.zanotti
 * @since 21/11/2018
 */
public abstract class BaseTransitionTask<TRANSITION,STATE, IDTYPE, MESSAGE, OPROC extends ObservableProcess<STATE, IDTYPE>> implements Runnable {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private TransitionNotifier<MESSAGE> transitionNotifier;
    protected TRANSITION transition;
    protected OPROC process;

    public BaseTransitionTask(TransitionNotifier<MESSAGE> transitionNotifier,
                              TRANSITION transition,
                              OPROC process) {
        this.transitionNotifier = transitionNotifier;
        this.transition = transition;
        this.process = process;
    }

    @Override
    public final void run() {
        logger.info("Start run");
        try {
            MESSAGE message = execute();
            transitionNotifier.notifyTransitionCompleted(message);
            logger.info("Completed run");
        } catch (Exception e) {
            logger.error("Run completed with error!", e);
        }
    }

    protected abstract MESSAGE execute();
}
