package es.amplia.springamqp;

import es.amplia.springamqp.configuration.AmqpProperties;
import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.AuditMessageNorth;
import es.amplia.springamqp.model.AuditMessageSouth;
import es.amplia.springamqp.model.builder.AuditMessageNorthBuilder;
import es.amplia.springamqp.model.builder.AuditMessageSouthBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collections;
import java.util.UUID;

public class SpringAmqpApplicationTests extends AbstractSpringBootTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringAmqpApplicationTests.class);

    @Autowired
    private AmqpProperties amqpProperties;

    @Autowired
    private RabbitTemplate amqpTemplate;

    @Autowired
    @Qualifier("auditMessageNorthConverter")
    private MessageConverter auditMessageNorthConverter;

    @Autowired
    @Qualifier("auditMessageSouthConverter")
    private MessageConverter auditMessageSouthConverter;

    @Test
    public void test () {
        AuditMessageNorth messageNorth = (AuditMessageNorth) AuditMessageNorthBuilder.builder()
                .userId(UUID.randomUUID())
                .component("component")
                .name("name")
                .type(AuditMessage.MsgType.REQUEST)
                .direction(AuditMessage.MsgDirection.OUT)
                .byteSize(100)
                .transactionId("transactionId")
                .status(AuditMessage.MsgStatus.SUCCESS)
                .subject("subject")
                .payload(Collections.<String, String>emptyMap())
                .build();

        LOGGER.debug("Sending message '{}'", messageNorth);
        amqpTemplate.setMessageConverter(auditMessageNorthConverter);
        amqpTemplate.convertAndSend(amqpProperties.getQueue().getAuditMessageNorth(), messageNorth);

        AuditMessageSouth messageSouth = (AuditMessageSouth) AuditMessageSouthBuilder.builder()
                .deviceId("deviceId")
                .component("component")
                .name("name")
                .type(AuditMessage.MsgType.REQUEST)
                .direction(AuditMessage.MsgDirection.OUT)
                .byteSize(100)
                .transactionId("transactionId")
                .status(AuditMessage.MsgStatus.SUCCESS)
                .subject("subject")
                .payload(Collections.<String, String>emptyMap())
                .build();
        LOGGER.debug("Sending message '{}'", messageSouth);
        amqpTemplate.setMessageConverter(auditMessageSouthConverter);
        amqpTemplate.convertAndSend(amqpProperties.getQueue().getAuditMessageSouth(), messageSouth);
    }
}
