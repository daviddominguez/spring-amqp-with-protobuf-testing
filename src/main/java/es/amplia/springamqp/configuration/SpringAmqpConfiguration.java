package es.amplia.springamqp.configuration;

import es.amplia.springamqp.Receiver;
import es.amplia.springamqp.converter.ProtobufMessageConverter;
import es.amplia.springamqp.model.serializer.AuditMessageNorthSerializer;
import es.amplia.springamqp.model.serializer.AuditMessageSouthSerializer;
import es.amplia.springamqp.protobuf.AuditMessageProtobuf;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
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
    SimpleMessageListenerContainer auditMessageNorthContainer(ConnectionFactory connectionFactory, @Qualifier("auditMessageNorthListenerAdapter") MessageListenerAdapter listenerAdapter) {
        return container(connectionFactory, listenerAdapter, NORTH_MESSAGE_QUEUE_NAME);
    }

    @Bean
    SimpleMessageListenerContainer auditMessageSouthContainer(ConnectionFactory connectionFactory, @Qualifier("auditMessageSouthListenerAdapter") MessageListenerAdapter listenerAdapter) {
        return container(connectionFactory, listenerAdapter, SOUTH_MESSAGE_QUEUE_NAME);
    }

    private SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter, String queueName) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageConverter auditMessageNorthConverter() {
        return new ProtobufMessageConverter<>(AuditMessageProtobuf.AuditMessageNorthProtobuf.getDescriptor().getFile(), new AuditMessageNorthSerializer());
    }

    @Bean
    public MessageConverter auditMessageSouthConverter() {
        return new ProtobufMessageConverter<>(AuditMessageProtobuf.AuditMessageSouthProtobuf.getDescriptor().getFile(), new AuditMessageSouthSerializer());
    }

    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    @Bean
    MessageListenerAdapter auditMessageNorthListenerAdapter(Receiver receiver) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, NORTH_MSG_METHOD_NAME);
        messageListenerAdapter.setMessageConverter(auditMessageNorthConverter());
        return messageListenerAdapter;
    }

    @Bean
    MessageListenerAdapter auditMessageSouthListenerAdapter(Receiver receiver) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, SOUTH_MSG_METHOD_NAME);
        messageListenerAdapter.setMessageConverter(auditMessageSouthConverter());
        return messageListenerAdapter;
    }
}