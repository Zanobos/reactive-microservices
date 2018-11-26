package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.OprocImpl;
import it.zano.microservices.observableprocess.OprocTransitionEnum;
import it.zano.microservices.observableprocess.TransitionNotifier;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public class OprocPutDocumentCompletedTask extends OprocBaseTask {

    public OprocPutDocumentCompletedTask(TransitionNotifier<OprocTransitionEnum, Integer> transitionNotifier,
                                         OprocTransitionEnum oprocTransitionEnum, OprocImpl process) {
        super(transitionNotifier, oprocTransitionEnum, process);
    }

    @Override
    protected void execute() {
        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            logger.error("Error");
        }
    }
}
