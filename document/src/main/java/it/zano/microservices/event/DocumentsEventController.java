package it.zano.microservices.event;

import it.zano.microservices.controller.event.RabbitController;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
@Service
public class DocumentsEventController extends RabbitController {

    @Autowired
    public DocumentsEventController(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    public void errorInSign(String documentId) {
        ErrorInSignMessage errorInSignMessage = new ErrorInSignMessage("signDoc","invalidDoc",documentId);
        super.sendToErrorExchange(errorInSignMessage);
    }
}
