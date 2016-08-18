package es.amplia.springamqp.sender;

import es.amplia.springamqp.AbstractSpringBootTest;
import es.amplia.springamqp.configuration.AmqpProperties;
import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.builder.AuditMessageBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class AuditMessagesServiceTest extends AbstractSpringBootTest {

    @Autowired
    private AmqpProperties amqpProperties;

    @Mock
    private RabbitTemplate amqpTemplateMock;

    @Autowired
    private MessageConverter auditMessageConverter;

    private AuditMessagesService auditMessageServiceMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        auditMessageServiceMock = new AuditMessagesServiceImpl(amqpProperties, amqpTemplateMock, auditMessageConverter);
    }

    @Test
    public void given_an_auditMessage_when_message_sent_to_queue_verify_amqpTemplate_is_invoked_with_correct_routing_key_and_message() {
        AuditMessage auditMessage = given_an_auditMessage();
        when_convertAndSend_invoked(auditMessage);
        auditMessageServiceMock.sendAuditMessage(auditMessage);
        verify_convertAndSend_is_invoked_once(auditMessage);
    }

    private AuditMessage given_an_auditMessage() {
        return AuditMessageBuilder.builder().build();
    }

    private void when_convertAndSend_invoked(AuditMessage auditMessage) {
        doNothing().when(amqpTemplateMock)
                .convertAndSend(eq(amqpProperties.getQueue().getAuditMessage()), eq(auditMessage));
    }

    private void verify_convertAndSend_is_invoked_once(AuditMessage auditMessage) {
        verify(amqpTemplateMock)
                .convertAndSend(eq(amqpProperties.getQueue().getAuditMessage()), eq(auditMessage));
    }
}
