package it.zano.microservices.event.observableprocess;

import it.zano.microservices.config.RabbitConfiguration;
import it.zano.microservices.controller.event.RabbitController;
import it.zano.microservices.event.EventConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Service
public class ObservableProcessEventPublisher extends RabbitController {

    private final static Logger logger = LoggerFactory.getLogger(ObservableProcessEventPublisher.class);

    @Autowired
    public ObservableProcessEventPublisher(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    public void sendMessage(EventTaskMessage message) {
        this.rabbitTemplate.convertAndSend(EventConfiguration.OPROC_EXCHANGE, RabbitConfiguration.FANOUT_ROUTING, message);
        logger.info("Sent {} to exchange {}", message, EventConfiguration.OPROC_EXCHANGE);
    }
}
