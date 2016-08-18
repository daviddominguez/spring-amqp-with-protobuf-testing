package es.amplia.springamqp.model.converter;

import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.builder.AuditMessageBuilder;
import es.amplia.springamqp.model.serializer.ProtobufSerializer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConverter;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class ProtobufMessageConverterTest {

    private static final String CONTENT_TYPE = "application/protobuf";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ProtobufSerializer<AuditMessage> serializer;

    private MessageConverter auditMessageConverter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        auditMessageConverter = new ProtobufMessageConverter<>(serializer);
    }

    @Test
    public void given_an_auditMessage_when_message_is_converted_to_amqpMessage_then_verify_its_properties_are_set() {
        AuditMessage auditMessage = given_an_auditMessage();
        when_serialize_invoked(auditMessage);
        verify_spring_message_returned_has_correct_properties_set(auditMessageConverter.toMessage(auditMessage, new MessageProperties()));
    }

    @Test
    public void given_a_well_formed_amqp_message_when_message_is_converted_to_auditMessage_then_verify_its_well_formed () {
        Message amqpMessage = given_an_amqp_message(CONTENT_TYPE);
        when_deserialize_invoked();
        verify_auditMessage_is_well_formed((AuditMessage) auditMessageConverter.fromMessage(amqpMessage));
    }

    @Test
    public void given_an_amqp_message_with_wrong_contentType_when_message_is_converted_to_auditMessage_then_exception_thrown () {
        expectedException.expect(AmqpRejectAndDontRequeueException.class);
        expectedException.expectCause(isA(IllegalArgumentException.class));
        expectedException.expectMessage("Cannot convert message");
        Message amqpMessage = given_an_amqp_message("bad_content_type");
        when_deserialize_invoked();
        auditMessageConverter.fromMessage(amqpMessage);
    }

    @Test
    public void given_an_amqp_message_with_wrong_payload_when_message_is_converted_to_auditMessage_then_exception_thrown () {
        expectedException.expect(AmqpRejectAndDontRequeueException.class);
        expectedException.expectCause(isA(RuntimeException.class));
        expectedException.expectMessage("Cannot convert message");
        Message amqpMessage = given_an_amqp_message(CONTENT_TYPE);
        when_deserialize_throws_exception();
        auditMessageConverter.fromMessage(amqpMessage);
    }

    private Message given_an_amqp_message(String contentType) {
        MessageProperties properties = new MessageProperties();
        properties.setContentType(contentType);
        return new Message(new byte[]{}, properties);
    }

    private AuditMessage given_an_auditMessage() {
        return AuditMessageBuilder.builder().build();
    }

    private void when_serialize_invoked(AuditMessage auditMessage) {
        when(serializer.serialize(eq(auditMessage))).thenReturn(new byte[]{});
    }

    private void when_deserialize_throws_exception () {
        when(serializer.deserialize(any(byte[].class))).thenThrow(new RuntimeException());
    }

    private void when_deserialize_invoked() {
        when(serializer.deserialize(any(byte[].class))).thenReturn(given_an_auditMessage());
    }

    private void verify_spring_message_returned_has_correct_properties_set(Message message) {
        assertThat(message.getMessageProperties().getContentType(), is("application/protobuf"));
        assertThat(message.getMessageProperties().getContentLength(), is(0L));
    }

    private void verify_auditMessage_is_well_formed(AuditMessage message) {
        assertThat(message, is(given_an_auditMessage()));
    }
}
