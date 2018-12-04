package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.EventNotifier;
import it.zano.microservices.observableprocess.OprocEventEnum;
import it.zano.microservices.observableprocess.OprocEventMessage;
import it.zano.microservices.observableprocess.OprocImpl;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public class OprocClearTask extends OprocBaseTask {

    public OprocClearTask(EventNotifier<OprocEventMessage> eventNotifier,
                          OprocEventEnum oprocEventEnum,
                          OprocImpl process) {
        super(eventNotifier, oprocEventEnum, process);
    }

    @Override
    protected void execute() {

        OprocEventMessage message = new OprocEventMessage(OprocEventEnum.CLEAR,process.getId());
        eventNotifier.notifyEvent(message);
    }
}
