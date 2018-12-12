package it.zano.microservices.event;

import it.zano.microservices.config.RabbitConfiguration;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
@Configuration
public class DocumentsEventConfiguration extends RabbitConfiguration {

    public final static String DOCUMENT_EXCHANGE = "documents";
    private final static String DOCUMENT_QUEUE = "documents";

    private final FanoutExchange documentExchange;
    private final Queue documentQueue;

    public DocumentsEventConfiguration() {
        super();
        documentExchange = new FanoutExchange(DOCUMENT_EXCHANGE);
        documentQueue = new Queue(DOCUMENT_QUEUE);
    }

    @Bean
    @Override
    public List<AbstractExchange> exchanges() {
        List<AbstractExchange> fanoutExchanges = super.exchanges();
        fanoutExchanges.add(documentExchange);
        return fanoutExchanges;
    }

    @Bean
    @Override
    public List<Queue> queues() {
        List<Queue> queues = super.queues();
        queues.add(documentQueue);
        return queues;
    }

    @Bean
    @Override
    public List<Binding> bindings() {
        List<Binding> bindings = super.bindings();
        bindings.add(BindingBuilder.bind(documentQueue).to(documentExchange));
        return bindings;
    }
}
