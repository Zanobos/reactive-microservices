package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.*;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public abstract class OprocBaseTask extends BaseTransitionTask<OprocTransitionEnum, OprocStateEnum, Integer, OprocTransitionMessage, OprocImpl> {

    public OprocBaseTask(TransitionNotifier<OprocTransitionMessage> transitionNotifier, OprocTransitionEnum oprocTransitionEnum, OprocImpl process) {
        super(transitionNotifier, oprocTransitionEnum, process);
    }

    protected OprocTransitionMessage defaultMessage() {
        return new OprocTransitionMessage(transition,process.getId());
    }
}
