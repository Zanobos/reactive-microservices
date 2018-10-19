package it.zano.microservices.controller.event;

import it.zano.microservices.config.RabbitConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public abstract class RabbitController {

    private RabbitTemplate rabbitTemplate;

    public RabbitController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToErrorExchange(ErrorMessage errorMessage) {
        this.rabbitTemplate.convertAndSend(RabbitConfiguration.ROLLBACK_TOPIC,RabbitConfiguration.FANOUT_ROUTING, errorMessage);
    }
}
