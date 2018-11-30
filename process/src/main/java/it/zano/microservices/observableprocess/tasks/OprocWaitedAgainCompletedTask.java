package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.OprocImpl;
import it.zano.microservices.observableprocess.OprocTransitionEnum;
import it.zano.microservices.observableprocess.OprocTransitionMessage;
import it.zano.microservices.observableprocess.TransitionNotifier;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public class OprocWaitedAgainCompletedTask extends OprocBaseTask {

    public OprocWaitedAgainCompletedTask(TransitionNotifier<OprocTransitionMessage> transitionNotifier,
                                         OprocTransitionEnum oprocTransitionEnum,
                                         OprocImpl process) {
        super(transitionNotifier, oprocTransitionEnum, process);
    }

    @Override
    protected OprocTransitionMessage execute() throws InterruptedException {
        //Do nothing
        Thread.sleep(10000);
        logger.info("Nothing to do");
        //Notify end
        return defaultMessage();
    }
}
