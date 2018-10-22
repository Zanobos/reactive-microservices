package it.zano.microservices.event;

import it.zano.microservices.config.RabbitConfiguration;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
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
    public final static String DOCUMENT_QUEUE = "documents";

    protected final FanoutExchange documentExchange;
    protected final Queue documentQueue;

    public DocumentsEventConfiguration() {
        documentExchange = new FanoutExchange(DOCUMENT_EXCHANGE);
        documentQueue = new Queue(DOCUMENT_QUEUE);
    }

    @Bean
    @Override
    public List<FanoutExchange> fanoutExchanges() {
        List<FanoutExchange> fanoutExchanges = super.fanoutExchanges();
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
