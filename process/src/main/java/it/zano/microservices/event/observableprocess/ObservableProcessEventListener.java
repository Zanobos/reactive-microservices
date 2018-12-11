package it.zano.microservices.event.observableprocess;

import it.zano.microservices.event.EventConfiguration;
import it.zano.microservices.observableprocess.OprocEventEnum;
import it.zano.microservices.observableprocess.OprocEventMessage;
import it.zano.microservices.observableprocess.OprocManagerImpl;
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

    private final OprocManagerImpl observableProcessManager;

    @Autowired
    public ObservableProcessEventListener(OprocManagerImpl observableProcessManager) {
        this.observableProcessManager = observableProcessManager;
    }

    @RabbitListener(queues = EventConfiguration.OPROC_QUEUE_MATCHER)
    public void handleRabbitMessage(@Payload OprocEventMessage message) {
        logger.info("Received message! {}", message);
        try {
            if(message.getEventEnum().equals(OprocEventEnum.CLEAR)) {
                observableProcessManager.clearResources(message.getProcessId());
            } else {
                observableProcessManager.executeEvent(message.getEventEnum(), message.getProcessId());
            }
        } catch (Exception e) {
            logger.error("Error in manager, but concluding gracefully so that message is removed");
        }
    }
}
