package it.zano.microservices.observableprocess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * True command pattern
 *
 * @author a.zanotti
 * @since 21/11/2018
 */
public abstract class BaseTask<EVENT, STATE, IDTYPE, MESSAGE, OPROC extends ObservableProcess<STATE, IDTYPE>> implements Runnable {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected EventNotifier<MESSAGE> eventNotifier;
    protected EVENT EVENT;
    protected OPROC process;

    public BaseTask(EventNotifier<MESSAGE> eventNotifier,
                    EVENT event,
                    OPROC process) {
        this.eventNotifier = eventNotifier;
        this.EVENT = event;
        this.process = process;
    }

    @Override
    public final void run() {
        logger.info("Start run");
        try {
            execute();
            logger.info("Completed run");
        } catch (Exception e) {
            logger.error("Run completed with error!", e);
        }
    }

    protected abstract void execute() throws InterruptedException;
}
