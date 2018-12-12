package it.zano.microservices.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public abstract class RabbitConfiguration implements RabbitListenerConfigurer {

    public static final String ROLLBACK_EXCHANGE = "rollback";
    public static final String FANOUT_ROUTING = "";
    public static final String ROLLBACK_QUEUE = "rollback";

    private final FanoutExchange rollbackExchange;
    private final Queue rollbackQueue;

    protected RabbitConfiguration() {
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


    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

}
