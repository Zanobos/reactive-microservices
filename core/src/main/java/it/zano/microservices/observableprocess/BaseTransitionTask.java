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
    private ThreadLocal<TRANSITION> transitionThreadLocal;
    private ThreadLocal<IDTYPE> idtypeThreadLocal;

    public BaseTransitionTask(TransitionNotifier<TRANSITION,IDTYPE> transitionNotifier,
                              TRANSITION transition,
                              IDTYPE id) {
        this.transitionNotifier = transitionNotifier;
        this.transitionThreadLocal = ThreadLocal.withInitial(() -> transition);
        this.idtypeThreadLocal = ThreadLocal.withInitial(() -> id);
    }

    @Override
    public final void run() {
        logger.info("Start run");
        execute();
        transitionNotifier.notifyTransition(transitionThreadLocal.get(),idtypeThreadLocal.get());
        logger.info("Completed run");
    }

    protected abstract void execute();
}
