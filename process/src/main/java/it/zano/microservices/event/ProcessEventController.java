package it.zano.microservices.event;

import it.zano.microservices.config.RabbitConfiguration;
import it.zano.microservices.controller.event.RabbitController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
@Service
public class ProcessEventController extends RabbitController {

    private final static Logger logger = LoggerFactory.getLogger(ProcessEventController.class);

    @Autowired
    public ProcessEventController(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }


    @RabbitListener(queues = RabbitConfiguration.ROLLBACK_QUEUE)
    public void handleRabbitMessage(@Payload ErrorInSignMessage errorMessage) {
        logger.info("Received message! {}",errorMessage);
    }
}
