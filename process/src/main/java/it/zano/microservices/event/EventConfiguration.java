package it.zano.microservices.event;

import it.zano.microservices.config.RabbitConfiguration;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
@Configuration
public class EventConfiguration extends RabbitConfiguration {

    public final static String DOCUMENT_EXCHANGE = "documents";
    public final static String DOCUMENT_QUEUE = "documents";

    private final FanoutExchange documentExchange;
    private final Queue documentQueue;

    public final static String OPROC_EXCHANGE = "oproc";
    public final static String OPROC_QUEUE = "oproc-" + UUID.randomUUID().toString(); //must be public for the matcher to found it
    public final static String OPROC_QUEUE_MATCHER = "#{T(it.zano.microservices.event.EventConfiguration).OPROC_QUEUE}";

    private final FanoutExchange oprocExchange;
    private final Queue oprocQueue;


    public EventConfiguration() {
        super();
        documentExchange = new FanoutExchange(DOCUMENT_EXCHANGE);
        documentQueue = new Queue(DOCUMENT_QUEUE);
        oprocExchange = new FanoutExchange(OPROC_EXCHANGE);
        oprocQueue = new Queue(OPROC_QUEUE, false, true, true);
    }

    @Bean
    @Override
    public List<AbstractExchange> exchanges() {
        List<AbstractExchange> fanoutExchanges = super.exchanges();
        fanoutExchanges.add(documentExchange);
        fanoutExchanges.add(oprocExchange);
        return fanoutExchanges;
    }

    @Bean
    @Override
    public List<Queue> queues() {
        List<Queue> queues = super.queues();
        queues.add(documentQueue);
        queues.add(oprocQueue);
        return queues;
    }

    @Bean
    @Override
    public List<Binding> bindings() {
        List<Binding> bindings = super.bindings();
        bindings.add(BindingBuilder.bind(documentQueue).to(documentExchange));
        bindings.add(BindingBuilder.bind(oprocQueue).to(oprocExchange));
        return bindings;
    }
}
