package it.zano.microservices.controller.event;

import it.zano.microservices.config.ExchangesConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public abstract class RabbitController {

    protected RabbitTemplate rabbitTemplate;

    public RabbitController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToErrorExchange(ErrorMessage errorMessage) {
        this.rabbitTemplate.convertAndSend(ExchangesConfiguration.ROLLBACK_EXCHANGE, ExchangesConfiguration.FANOUT_ROUTING, errorMessage);
    }
}
