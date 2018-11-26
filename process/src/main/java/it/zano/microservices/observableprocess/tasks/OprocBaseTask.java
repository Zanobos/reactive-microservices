package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.*;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public abstract class OprocBaseTask extends BaseTransitionTask<OprocTransitionEnum, OprocStateEnum,Integer, OprocImpl> {

    public OprocBaseTask(TransitionNotifier<OprocTransitionEnum, Integer> transitionNotifier, OprocTransitionEnum oprocTransitionEnum, OprocImpl process) {
        super(transitionNotifier, oprocTransitionEnum, process);
    }
}
