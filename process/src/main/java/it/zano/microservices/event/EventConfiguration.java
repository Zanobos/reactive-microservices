package it.zano.microservices.event;

import it.zano.microservices.config.RabbitConfiguration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
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

    protected final FanoutExchange documentExchange;
    protected final Queue documentQueue;

    public final static String OPROC_EXCHANGE = "oproc";
    public final static String OPROC_QUEUE = "oproc-" + UUID.randomUUID().toString();
    public final static String OPROC_QUEUE_MATCHER = "#{T(it.zano.microservices.event.EventConfiguration).OPROC_QUEUE}";

    protected final FanoutExchange oprocExchange;
    protected final Queue oprocQueue;


    public EventConfiguration() {
        documentExchange = new FanoutExchange(DOCUMENT_EXCHANGE);
        documentQueue = new Queue(DOCUMENT_QUEUE);
        oprocExchange = new FanoutExchange(OPROC_EXCHANGE);
        oprocQueue = new Queue(OPROC_QUEUE, false, true, true);
    }

    @Bean
    @Override
    public List<FanoutExchange> fanoutExchanges() {
        List<FanoutExchange> fanoutExchanges = super.fanoutExchanges();
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
