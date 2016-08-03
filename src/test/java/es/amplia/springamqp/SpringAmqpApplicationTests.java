package es.amplia.springamqp;

import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.AuditMessageNorth;
import es.amplia.springamqp.model.AuditMessageSouth;
import es.amplia.springamqp.model.builder.AuditMessageNorthBuilder;
import es.amplia.springamqp.model.builder.AuditMessageSouthBuilder;
import es.amplia.springamqp.sender.AuditMessagesService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.UUID;

public class SpringAmqpApplicationTests extends AbstractSpringBootTest {

    @Autowired
    private AuditMessagesService messagesService;

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
        messagesService.sendAuditMessageNorth(messageNorth);

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
        messagesService.sendAuditMessageSouth(messageSouth);
    }
}
