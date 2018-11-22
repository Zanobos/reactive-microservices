package it.zano.microservices.dispatcher.tasks;

import it.zano.microservices.event.observableprocess.EventTaskMessage;
import it.zano.microservices.event.observableprocess.ObservableProcessEventPublisher;

import static it.zano.microservices.model.beans.ObservableProcessTransitionEnum.WAITED_COMPLETED;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public class PutDocumentCompletedEventTask extends BaseTask{

    private ObservableProcessEventPublisher eventPublisher;
    private Integer id;

    public PutDocumentCompletedEventTask(Integer id, ObservableProcessEventPublisher eventPublisher) {
        this.id = id;
        this.eventPublisher = eventPublisher;
    }

    @Override
    protected void execute() {
        try {
            Thread.sleep(12000);
            EventTaskMessage message = new EventTaskMessage();
            message.setProcessId(id);
            message.setTransition(WAITED_COMPLETED);
            eventPublisher.sendMessage(message);
        } catch (InterruptedException e) {
            logger.error("Error");
        }
    }
}
