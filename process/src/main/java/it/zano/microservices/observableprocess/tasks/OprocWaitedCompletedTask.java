package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.OprocTransitionEnum;
import it.zano.microservices.observableprocess.TransitionNotifier;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public class OprocWaitedCompletedTask extends OprocBaseTask {

    public OprocWaitedCompletedTask(TransitionNotifier<OprocTransitionEnum, Integer> transitionNotifier, OprocTransitionEnum oprocTransitionEnum, Integer id) {
        super(transitionNotifier, oprocTransitionEnum, id);
    }

    @Override
    protected void execute() {
        //Do nothing
        logger.info("Nothing to do");
    }
}
