package es.amplia.springamqp;

import com.google.protobuf.Message;
import es.amplia.springamqp.configuration.SpringAmqpConfiguration;
import es.amplia.springamqp.protobuf.AuditMessageProtobuf;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class SpringAmqpApplicationTests extends AbstractSpringBootTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringAmqpApplicationTests.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void test () {
        Message messageNorth = AuditMessageProtobuf.AuditMessageNorthProtobuf.newBuilder()
                .setComponent("component")
                .setName("name")
                .setType(AuditMessageProtobuf.AuditMessageNorthProtobuf.MsgType.REQUEST)
                .setDirection(AuditMessageProtobuf.AuditMessageNorthProtobuf.MsgDirection.OUT)
                .setByteSize(100)
                .setTransactionId("transactionId")
                .setStatus(AuditMessageProtobuf.AuditMessageNorthProtobuf.MsgStatus.SUCCESS)
                .setSubject("subject")
                .build();
        LOGGER.debug("Sending message '{}'", messageNorth);
        amqpTemplate.convertAndSend(SpringAmqpConfiguration.EXCHANGE, SpringAmqpConfiguration.NORTH_MESSAGE_ROUTING_KEY, messageNorth);

        Message messageSouth = AuditMessageProtobuf.AuditMessageSouthProtobuf.newBuilder()
                .setComponent("component")
                .setName("name")
                .setType(AuditMessageProtobuf.AuditMessageSouthProtobuf.MsgType.REQUEST)
                .setDirection(AuditMessageProtobuf.AuditMessageSouthProtobuf.MsgDirection.OUT)
                .setByteSize(100)
                .setTransactionId("transactionId")
                .setStatus(AuditMessageProtobuf.AuditMessageSouthProtobuf.MsgStatus.SUCCESS)
                .setSubject("subject")
                .build();
        LOGGER.debug("Sending message '{}'", messageSouth);
        amqpTemplate.convertAndSend(SpringAmqpConfiguration.EXCHANGE, SpringAmqpConfiguration.SOUTH_MESSAGE_ROUTING_KEY, messageSouth);

    }
}
