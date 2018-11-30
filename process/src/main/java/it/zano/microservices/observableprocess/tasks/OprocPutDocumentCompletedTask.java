package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.OprocImpl;
import it.zano.microservices.observableprocess.OprocTransitionEnum;
import it.zano.microservices.observableprocess.OprocTransitionMessage;
import it.zano.microservices.observableprocess.TransitionNotifier;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public class OprocPutDocumentCompletedTask extends OprocBaseTask {

    public OprocPutDocumentCompletedTask(TransitionNotifier<OprocTransitionMessage> transitionNotifier,
                                         OprocTransitionEnum oprocTransitionEnum,
                                         OprocImpl process) {
        super(transitionNotifier, oprocTransitionEnum, process);
    }

    @Override
    protected OprocTransitionMessage execute() {
        try {
            Thread.sleep(7000);
            transitionNotifier.notifyTransitionCompleted(defaultMessage());
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error("Error");
        }
        return new OprocTransitionMessage(OprocTransitionEnum.WAITED_COMPLETED,process.getId());
    }
}
