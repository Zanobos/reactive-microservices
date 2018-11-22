package it.zano.microservices.dispatcher.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * True command pattern
 * @author a.zanotti
 * @since 21/11/2018
 */
public abstract class BaseTask implements Runnable {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public final void run() {
        logger.info("Start run");
        execute();
        logger.info("Completed run");
    }

    protected abstract void execute();
}
