package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.BaseTransitionTask;
import it.zano.microservices.observableprocess.OprocTransitionEnum;
import it.zano.microservices.observableprocess.TransitionNotifier;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public abstract class OprocBaseTask extends BaseTransitionTask<OprocTransitionEnum, Integer> {

    public OprocBaseTask(TransitionNotifier<OprocTransitionEnum, Integer> transitionNotifier, OprocTransitionEnum oprocTransitionEnum, Integer id) {
        super(transitionNotifier, oprocTransitionEnum, id);
    }
}
