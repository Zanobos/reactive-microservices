package it.zano.microservices.observableprocess;

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
 * @since 23/11/2018
 */
@Service
public class OprocEventNotifierImpl extends RabbitController
        implements EventNotifier<OprocEventMessage> {

    private final static Logger logger = LoggerFactory.getLogger(OprocEventNotifierImpl.class);

    @Autowired
    public OprocEventNotifierImpl(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    @Override
    public void notifyEvent(OprocEventMessage message) {
        rabbitTemplate.convertAndSend(EventConfiguration.OPROC_EXCHANGE, RabbitConfiguration.FANOUT_ROUTING, message);
        logger.info("Sent {} to exchange {}", message, EventConfiguration.OPROC_EXCHANGE);
    }
}
