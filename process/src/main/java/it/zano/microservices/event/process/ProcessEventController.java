package it.zano.microservices.event.process;

import it.zano.microservices.controller.event.RabbitController;
import it.zano.microservices.event.EventConfiguration;
import it.zano.microservices.model.entities.User;
import it.zano.microservices.model.repositories.UserRepository;
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
    public ProcessEventController(RabbitTemplate rabbitTemplate, UserRepository userRepository) {
        super(rabbitTemplate);
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = EventConfiguration.DOCUMENT_QUEUE)
    public void handleRabbitMessage(@Payload SignDocMessage message) {
        logger.info("Received message! {}",message);
        User user = userRepository.findById(Integer.parseInt(message.getUserId())).orElse(null);
        if(user == null) {
            ErrorInSignMessage errorInSignMessage = new ErrorInSignMessage("signDoc","nouserfound",message.getUserId());
            sendToErrorExchange(errorInSignMessage);
        }

    }

    private final UserRepository userRepository;
}
