package it.zano.microservices.observableprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * True command pattern
 * @author a.zanotti
 * @since 21/11/2018
 */
public abstract class BaseTransitionTask<TRANSITION,IDTYPE> implements Runnable {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private TransitionNotifier<TRANSITION,IDTYPE> transitionNotifier;
    protected ThreadLocal<TRANSITION> transition;
    protected ThreadLocal<IDTYPE> id;

    public BaseTransitionTask(TransitionNotifier<TRANSITION,IDTYPE> transitionNotifier,
                              TRANSITION transition,
                              IDTYPE id) {
        this.transitionNotifier = transitionNotifier;
        this.transition = ThreadLocal.withInitial(() -> transition);
        this.id = ThreadLocal.withInitial(() -> id);
    }

    @Override
    public final void run() {
        logger.info("Start run");
        execute();
        transitionNotifier.notifyTransitionCompleted(transition.get(), id.get());
        logger.info("Completed run");
    }

    protected abstract void execute();
}
