package es.amplia.springamqp.configuration;

import es.amplia.springamqp.Receiver;
import es.amplia.springamqp.converter.ProtobufMessageConverter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static es.amplia.springamqp.Receiver.NORTH_MSG_METHOD_NAME;
import static es.amplia.springamqp.Receiver.SOUTH_MSG_METHOD_NAME;
import static java.util.Arrays.asList;
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@Configuration
public class SpringAmqpConfiguration {

    public static final String EXCHANGE = "exchange_name";
    public static final String NORTH_MESSAGE_ROUTING_KEY = "north_msg_routing_key";
    public static final String SOUTH_MESSAGE_ROUTING_KEY = "south_msg_routing_key";

    private static final String NORTH_MESSAGE_QUEUE_NAME = "north_msg_queue";
    private static final String SOUTH_MESSAGE_QUEUE_NAME = "south_msg_queue";

    private static final boolean IS_DURABLE = false;
    public static final boolean IS_AUTODELETE = true;

    @Bean
    List<Queue> queues() {
        return asList(
                new Queue(NORTH_MESSAGE_QUEUE_NAME, IS_DURABLE, false, IS_AUTODELETE),
                new Queue(SOUTH_MESSAGE_QUEUE_NAME, IS_DURABLE, false, IS_AUTODELETE)
        );
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE, IS_DURABLE, IS_AUTODELETE);
    }

    @Bean
    List<Binding> bindings() {
        // return BindingBuilder.bind(queue).to(exchange).with(NORTH_MESSAGE_ROUTING_KEY);
        return asList(
                new Binding(NORTH_MESSAGE_QUEUE_NAME, QUEUE, EXCHANGE, NORTH_MESSAGE_ROUTING_KEY, null),
                new Binding(SOUTH_MESSAGE_QUEUE_NAME, QUEUE, EXCHANGE, SOUTH_MESSAGE_ROUTING_KEY, null)
        );
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(NORTH_MESSAGE_QUEUE_NAME, SOUTH_MESSAGE_QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new ProtobufMessageConverter();
    }

    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, Receiver.DEFAULT_RECEIVE_METHOD_NAME);
        messageListenerAdapter.addQueueOrTagToMethodName(NORTH_MESSAGE_QUEUE_NAME, NORTH_MSG_METHOD_NAME);
        messageListenerAdapter.addQueueOrTagToMethodName(SOUTH_MESSAGE_QUEUE_NAME, SOUTH_MSG_METHOD_NAME);
        messageListenerAdapter.setMessageConverter(messageConverter());
        return messageListenerAdapter;
    }
}