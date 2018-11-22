package it.zano.microservices.event.observableprocess;

import it.zano.microservices.dispatcher.ObservableProcessManager;
import it.zano.microservices.event.EventConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Service
public class ObservableProcessEventListener{

    private final static Logger logger = LoggerFactory.getLogger(ObservableProcessEventListener.class);

    @Autowired
    public ObservableProcessEventListener(ObservableProcessManager observableProcessManager) {
        this.observableProcessManager = observableProcessManager;
    }

    @RabbitListener(queues = EventConfiguration.OPROC_QUEUE)
    public void handleRabbitMessage(@Payload EventTaskMessage message) {
        logger.info("Received message! {}", message);
        try {
            observableProcessManager.executeEvent(message.getTransition(), message.getProcessId());
        } catch (Exception e) {
            logger.error("Error in manager, but concluding gracefully so that message is removed");
        }
    }

    private ObservableProcessManager observableProcessManager;
}
