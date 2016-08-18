package es.amplia.springamqp.sender;

import es.amplia.springamqp.configuration.AmqpProperties;
import es.amplia.springamqp.model.AuditMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class AuditMessagesServiceImpl implements AuditMessagesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditMessagesServiceImpl.class);

    private final AmqpProperties amqpProperties;
    private final RabbitTemplate amqpTemplate;

    @Autowired
    public AuditMessagesServiceImpl(
            AmqpProperties amqpProperties,
            RabbitTemplate amqpTemplate,
            MessageConverter auditMessageConverter) {
        this.amqpProperties = amqpProperties;
        this.amqpTemplate = amqpTemplate;
        amqpTemplate.setMessageConverter(auditMessageConverter);
    }

    @Override
    public void sendAuditMessage(AuditMessage message) {
        LOGGER.debug("Sending message '{}'", message);
        amqpTemplate.convertAndSend(amqpProperties.getQueue().getAuditMessage(), message);
    }
}
