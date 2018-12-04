package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.*;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public abstract class OprocBaseTask extends BaseTask<OprocEventEnum, OprocStateEnum, Integer, OprocEventMessage, OprocImpl> {

    public OprocBaseTask(EventNotifier<OprocEventMessage> eventNotifier, OprocEventEnum oprocEventEnum, OprocImpl process) {
        super(eventNotifier, oprocEventEnum, process);
    }

}
