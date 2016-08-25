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
                .msgName(AuditMessage.NameType.OPERATION)
                .msgType(AuditMessage.MsgType.REQUEST)
                .msgDirection(AuditMessage.MsgDirection.OUT)
                .subject("subject")
                .subjectType(AuditMessage.SubjectType.DEVICE)
                .user("user")
                .transactionId("transactionId")
                .sequenceId("sequenceId")
                .msgStatus(AuditMessage.MsgStatus.SUCCESS)
                .msgSizeBytes(100)
                .msgContext(Collections.<String, String>emptyMap())
                .timestamp(new Date())
                .version(0)
                .build();
        messagesService.sendAuditMessage(messageNorth);
    }
}
