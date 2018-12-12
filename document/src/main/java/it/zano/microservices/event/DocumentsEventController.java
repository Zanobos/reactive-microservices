package it.zano.microservices.event;

import it.zano.microservices.config.ExchangesConfiguration;
import it.zano.microservices.controller.event.RabbitController;
import it.zano.microservices.model.entities.Document;
import it.zano.microservices.model.repositories.DocumentRepository;
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
public class DocumentsEventController extends RabbitController {

    private final static Logger logger = LoggerFactory.getLogger(DocumentsEventController.class);

    @Autowired
    public DocumentsEventController(RabbitTemplate rabbitTemplate, DocumentRepository documentRepository) {
        super(rabbitTemplate);
        this.documentRepository = documentRepository;
    }

    public void signedDocumentFor(String documentId, String userId) {
        SignDocMessage signDocMessage = new SignDocMessage(documentId,userId);
        rabbitTemplate.convertAndSend(DocumentsEventConfiguration.DOCUMENT_EXCHANGE, ExchangesConfiguration.FANOUT_ROUTING,signDocMessage);
    }

    @RabbitListener(queues = ExchangesConfiguration.ROLLBACK_QUEUE)
    public void handleRabbitMessage(@Payload ErrorInSignMessage errorMessage) {
        logger.info("Received message! {}",errorMessage);
        Document document = documentRepository.findById(Integer.parseInt(errorMessage.getDocumentId())).orElse(null);
        if(document != null) {
            logger.info("Rollbacking {}", document);
            document.setSignatureActual(null);
            documentRepository.save(document);
        }
    }

    private final DocumentRepository documentRepository;

}
