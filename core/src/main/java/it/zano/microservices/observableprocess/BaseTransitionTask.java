package it.zano.microservices.observableprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * True command pattern
 * @author a.zanotti
 * @since 21/11/2018
 */
public abstract class BaseTransitionTask<TRANSITION,STATE, IDTYPE, OPROC extends ObservableProcess<STATE, IDTYPE>> implements Runnable {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private TransitionNotifier<TRANSITION,IDTYPE> transitionNotifier;
    protected ThreadLocal<TRANSITION> transition;
    protected ThreadLocal<OPROC> process;

    public BaseTransitionTask(TransitionNotifier<TRANSITION,IDTYPE> transitionNotifier,
                              TRANSITION transition,
                              OPROC process) {
        this.transitionNotifier = transitionNotifier;
        this.transition = ThreadLocal.withInitial(() -> transition);
        this.process = ThreadLocal.withInitial(() -> process);
    }

    @Override
    public final void run() {
        logger.info("Start run");
        try {
            execute();
            transitionNotifier.notifyTransitionCompleted(transition.get(), process.get().getId());
            logger.info("Completed run");
        } catch (Exception e) {
            logger.error("Run completed with error!", e);
        }
    }

    protected abstract void execute();
}
