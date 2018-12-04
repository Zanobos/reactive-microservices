package it.zano.microservices.observableprocess.tasks;

import it.zano.microservices.observableprocess.EventNotifier;
import it.zano.microservices.observableprocess.OprocEventEnum;
import it.zano.microservices.observableprocess.OprocEventMessage;
import it.zano.microservices.observableprocess.OprocImpl;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public class OprocPutDocumentCompletedTask extends OprocBaseTask {

    public OprocPutDocumentCompletedTask(EventNotifier<OprocEventMessage> eventNotifier,
                                         OprocEventEnum oprocEventEnum,
                                         OprocImpl process) {
        super(eventNotifier, oprocEventEnum, process);
    }

    @Override
    protected void execute() {
        try {
            Thread.sleep(7000);
            OprocEventMessage message = new OprocEventMessage(OprocEventEnum.WAITED_COMPLETED, process.getId());
            eventNotifier.notifyEvent(message);
            Thread.sleep(3000);
            OprocEventMessage lastMessage = new OprocEventMessage(OprocEventEnum.AGAIN_WAITING_COMPLETED, process.getId());
            eventNotifier.notifyEvent(lastMessage);
        } catch (InterruptedException e) {
            logger.error("Error");
        }
    }
}
