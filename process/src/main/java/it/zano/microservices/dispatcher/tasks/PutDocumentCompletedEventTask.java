package it.zano.microservices.dispatcher.tasks;

import it.zano.microservices.dispatcher.ObservableProcessManager;
import it.zano.microservices.event.observableprocess.EventTaskMessage;
import it.zano.microservices.event.observableprocess.ObservableProcessEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public class PutDocumentCompletedEventTask implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(PutDocumentCompletedEventTask.class);

    private ObservableProcessEventPublisher eventPublisher;
    private Integer id;

    public PutDocumentCompletedEventTask(Integer id, ObservableProcessEventPublisher eventPublisher) {
        this.id = id;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            EventTaskMessage message = new EventTaskMessage();
            message.setProcessId(id);
            message.setEventTypeEnum(ObservableProcessManager.EventTypeEnum.WAITED_COMPLETED);
            eventPublisher.sendMessage(message);
            logger.info("Completed run");
        } catch (InterruptedException e) {
            logger.error("Error");
        }
    }
}
