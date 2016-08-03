package es.amplia.springamqp.sender;

import es.amplia.springamqp.configuration.AmqpProperties;
import es.amplia.springamqp.model.AuditMessageNorth;
import es.amplia.springamqp.model.AuditMessageSouth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AuditMessagesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditMessagesService.class);

    private final AmqpProperties amqpProperties;
    private final RabbitTemplate amqpTemplate;
    private final MessageConverter auditMessageNorthConverter;
    private final MessageConverter auditMessageSouthConverter;

    @Autowired
    public AuditMessagesService(
            AmqpProperties amqpProperties,
            RabbitTemplate amqpTemplate,
            @Qualifier("auditMessageSouthConverter") MessageConverter auditMessageSouthConverter,
            @Qualifier("auditMessageNorthConverter") MessageConverter auditMessageNorthConverter) {
        this.amqpProperties = amqpProperties;
        this.auditMessageSouthConverter = auditMessageSouthConverter;
        this.amqpTemplate = amqpTemplate;
        this.auditMessageNorthConverter = auditMessageNorthConverter;
    }

    public void sendAuditMessageNorth(AuditMessageNorth messageNorth) {
        LOGGER.debug("Sending message '{}'", messageNorth);
        amqpTemplate.setMessageConverter(auditMessageNorthConverter);
        amqpTemplate.convertAndSend(amqpProperties.getQueue().getAuditMessageNorth(), messageNorth);
    }

    public void sendAuditMessageSouth(AuditMessageSouth messageSouth) {
        LOGGER.debug("Sending message '{}'", messageSouth);
        amqpTemplate.setMessageConverter(auditMessageSouthConverter);
        amqpTemplate.convertAndSend(amqpProperties.getQueue().getAuditMessageSouth(), messageSouth);
    }
}
