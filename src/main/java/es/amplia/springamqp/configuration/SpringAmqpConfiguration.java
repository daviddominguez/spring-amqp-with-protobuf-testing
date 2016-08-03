package es.amplia.springamqp.configuration;

import es.amplia.springamqp.Receiver;
import es.amplia.springamqp.converter.ProtobufMessageConverter;
import es.amplia.springamqp.model.serializer.AuditMessageNorthSerializer;
import es.amplia.springamqp.model.serializer.AuditMessageSouthSerializer;
import es.amplia.springamqp.protobuf.AuditMessageProtobuf;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static es.amplia.springamqp.Receiver.NORTH_MSG_METHOD_NAME;
import static es.amplia.springamqp.Receiver.SOUTH_MSG_METHOD_NAME;
import static java.util.Arrays.asList;
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@Configuration
@EnableConfigurationProperties(AmqpProperties.class)
public class SpringAmqpConfiguration {

    @Autowired
    private AmqpProperties amqpProperties;

    @Bean
    List<Queue> queues() {
        return asList(
                new Queue(amqpProperties.getQueue().getAuditMessageNorth()),
                new Queue(amqpProperties.getQueue().getAuditMessageSouth())
        );
    }

    @Bean
    List<Binding> bindings() {
        return asList(
                new Binding(amqpProperties.getQueue().getAuditMessageNorth(), QUEUE, "",
                        amqpProperties.getQueue().getAuditMessageNorth(), null),
                new Binding(amqpProperties.getQueue().getAuditMessageSouth(), QUEUE, "",
                        amqpProperties.getQueue().getAuditMessageSouth(), null)
        );
    }

    @Bean
    SimpleMessageListenerContainer auditMessageNorthContainer(
            ConnectionFactory connectionFactory,
            @Qualifier("auditMessageNorthListenerAdapter") MessageListenerAdapter listenerAdapter
    ) {
        return container(connectionFactory, listenerAdapter, amqpProperties.getQueue().getAuditMessageNorth());
    }

    @Bean
    SimpleMessageListenerContainer auditMessageSouthContainer(
            ConnectionFactory connectionFactory,
            @Qualifier("auditMessageSouthListenerAdapter") MessageListenerAdapter listenerAdapter
    ) {
        return container(connectionFactory, listenerAdapter, amqpProperties.getQueue().getAuditMessageSouth());
    }

    private SimpleMessageListenerContainer container(
            ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter,
            String queueName
    ) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageConverter auditMessageNorthConverter() {
        return new ProtobufMessageConverter<>(
                AuditMessageProtobuf.AuditMessageNorthProtobuf.getDescriptor().getFile(),
                new AuditMessageNorthSerializer());
    }

    @Bean
    public MessageConverter auditMessageSouthConverter() {
        return new ProtobufMessageConverter<>(
                AuditMessageProtobuf.AuditMessageSouthProtobuf.getDescriptor().getFile(),
                new AuditMessageSouthSerializer());
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