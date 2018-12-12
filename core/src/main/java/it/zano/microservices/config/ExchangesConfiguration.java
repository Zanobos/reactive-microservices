package it.zano.microservices.config;

import org.springframework.amqp.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public abstract class ExchangesConfiguration {

    public static final String ROLLBACK_EXCHANGE = "rollback";
    public static final String FANOUT_ROUTING = "";
    public static final String ROLLBACK_QUEUE = "rollback";

    private final FanoutExchange rollbackExchange;
    private final Queue rollbackQueue;

    protected ExchangesConfiguration() {
        rollbackExchange = new FanoutExchange(ROLLBACK_EXCHANGE);
        rollbackQueue = new Queue(ROLLBACK_QUEUE);
    }

    protected List<AbstractExchange> exchanges() {
        List<AbstractExchange> fanoutExchanges = new ArrayList<>();
        fanoutExchanges.add(rollbackExchange);
        return fanoutExchanges;
    }

    protected List<Queue> queues() {
        List<Queue> queues = new ArrayList<>();
        queues.add(rollbackQueue);
        return queues;
    }

    protected List<Binding> bindings() {
        List<Binding> bindings = new ArrayList<>();
        bindings.add(BindingBuilder.bind(rollbackQueue).to(rollbackExchange));
        return bindings;
    }

}
