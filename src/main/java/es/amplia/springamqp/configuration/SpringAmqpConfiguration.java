package es.amplia.springamqp.configuration;

import es.amplia.springamqp.Receiver;
import es.amplia.springamqp.model.converter.ProtobufMessageConverter;
import es.amplia.springamqp.model.serializer.AuditMessageSerializer;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static es.amplia.springamqp.Receiver.AUDIT_MESSAGE_METHOD_NAME;
import static org.springframework.amqp.core.BindingBuilder.bind;

@Configuration
@EnableConfigurationProperties(AmqpProperties.class)
public class SpringAmqpConfiguration {

    @Autowired
    private AmqpProperties amqpProperties;

    @Bean
    Queue queue() {
        return new Queue(amqpProperties.getQueue().getAuditMessage());
    }

    @Bean
    Exchange defaultExchange() {
        return new DirectExchange("");
    }
    @Bean
    Binding binding(Queue queue, Exchange defaultExchange) {
        return bind(queue).to(defaultExchange).with(amqpProperties.getQueue().getAuditMessage()).noargs();
    }

    @Bean
    SimpleMessageListenerContainer auditMessageContainer(
            ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter
    ) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(amqpProperties.getQueue().getAuditMessage());
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageConverter auditMessageConverter() {
        return new ProtobufMessageConverter<>(new AuditMessageSerializer());
    }

    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    @Bean
    MessageListenerAdapter auditMessageListenerAdapter(Receiver receiver) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiver, AUDIT_MESSAGE_METHOD_NAME);
        messageListenerAdapter.setMessageConverter(auditMessageConverter());
        return messageListenerAdapter;
    }
}