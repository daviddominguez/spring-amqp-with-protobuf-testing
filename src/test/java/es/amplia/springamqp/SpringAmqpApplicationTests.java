package es.amplia.springamqp;

import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.builder.AuditMessageBuilder;
import es.amplia.springamqp.sender.AuditMessagesService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;

public class SpringAmqpApplicationTests extends AbstractSpringBootTest {

    @Autowired
    private AuditMessagesService messagesService;

    @Test
    public void test () {
        AuditMessage messageNorth = AuditMessageBuilder.builder()
                .process(AuditMessage.ProcessType.CONNECTOR)
                .component(AuditMessage.ComponentType.WEBSOCKET)
                .name(AuditMessage.NameType.OPERATION)
                .type(AuditMessage.MsgType.REQUEST)
                .direction(AuditMessage.MsgDirection.OUT)
                .subject("subject")
                .subjectType(AuditMessage.SubjectType.DEVICE)
                .user("user")
                .transactionId("transactionId")
                .sequenceId("sequenceId")
                .status(AuditMessage.MsgStatus.SUCCESS)
                .byteSize(100)
                .payload(Collections.<String, String>emptyMap())
                .createdDateTime(new Date())
                .version(0)
                .build();
        messagesService.sendAuditMessage(messageNorth);
    }
}
