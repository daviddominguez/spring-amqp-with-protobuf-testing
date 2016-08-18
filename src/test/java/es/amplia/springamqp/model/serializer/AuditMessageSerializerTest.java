package es.amplia.springamqp.model.serializer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.builder.AuditMessageBuilder;
import es.amplia.springamqp.protobuf.AuditMessageProto;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static es.amplia.springamqp.model.AuditMessage.ComponentType.WEBSOCKET;
import static es.amplia.springamqp.model.AuditMessage.MsgDirection.IN;
import static es.amplia.springamqp.model.AuditMessage.MsgStatus.SUCCESS;
import static es.amplia.springamqp.model.AuditMessage.MsgType.RESPONSE;
import static es.amplia.springamqp.model.AuditMessage.NameType.DMM;
import static es.amplia.springamqp.model.AuditMessage.ProcessType.ALARM;
import static es.amplia.springamqp.model.AuditMessage.SubjectType.IMSI;
import static java.util.Collections.singletonMap;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class AuditMessageSerializerTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final String TIMESTAMP = "2016-01-01T0:00:00.000";
    private AuditMessageSerializer serializer = new AuditMessageSerializer();

    @Test
    public void given_an_auditMessage_without_any_attribute_set_when_serialized_then_exception_thrown () throws ParseException, InvalidProtocolBufferException {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("date cannot be null");
        AuditMessage auditMessage = given_an_auditMessage_without_any_attribute_set();
        AuditMessageProto.AuditMessage auditMessageProto = AuditMessageProto.AuditMessage.parseFrom(serializer.serialize(auditMessage));
        verify_that_auditMessageProto_has_all_attributes_set(auditMessageProto);
    }

    @Test
    public void given_an_auditMessage_with_all_attributes_set_when_serialized_then_auditMessageProto_has_all_attributes_set() throws ParseException, InvalidProtocolBufferException {
        AuditMessage auditMessage = given_an_auditMessage_with_all_attributes_set();
        AuditMessageProto.AuditMessage auditMessageProto = AuditMessageProto.AuditMessage.parseFrom(serializer.serialize(auditMessage));
        verify_that_auditMessageProto_has_all_attributes_set(auditMessageProto);
    }

    @Test
    public void given_an_auditMessage_without_any_attribute_set_except_dateTime_when_serialized_then_auditMessageProto_has_all_attributes_set_to_default() throws InvalidProtocolBufferException, ParseException {
        AuditMessage auditMessage = given_an_auditMessage_without_any_attribute_set_except_dateTime();
        AuditMessageProto.AuditMessage auditMessageProto = AuditMessageProto.AuditMessage.parseFrom(serializer.serialize(auditMessage));
        verify_that_auditMessageProto_has_all_attributes_set_to_default(auditMessageProto);
    }

    @Test
    public void given_an_auditMessageProto_with_all_attributes_set_to_default_value_when_deserialized_then_auditMessage_has_all_attributes_unset() throws ParseException {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("timestamp cannot be empty");
        AuditMessageProto.AuditMessage auditMessageProto = given_an_auditMessageProto_with_all_attributes_set_to_default_value();
        AuditMessage auditMessage = serializer.deserialize(auditMessageProto.toByteArray());
        verify_that_auditMessage_has_all_attributes_null(auditMessage);
    }

    @Test
    public void given_an_auditMessageProto_with_all_attributes_set_to_default_value_except_timestamp_when_deserialized_then_auditMessage_has_all_attributes_unset() throws ParseException {
        AuditMessageProto.AuditMessage auditMessageProto = given_an_auditMessageProto_with_all_attributes_set_to_default_value_except_timestamp();
        AuditMessage auditMessage = serializer.deserialize(auditMessageProto.toByteArray());
        verify_that_auditMessage_has_all_attributes_null(auditMessage);
    }

    @Test
    public void given_an_auditMessageProto_with_all_attributes_set_to_other_than_default_value_when_deserialized_then_auditMessage_has_all_attributes_set() throws ParseException {
        AuditMessageProto.AuditMessage auditMessageProto = given_an_auditMessageProto_with_all_attributes_set_to_other_than_default_value();
        AuditMessage auditMessage = serializer.deserialize(auditMessageProto.toByteArray());
        verify_that_auditMessage_has_all_attributes_set(auditMessage);
    }

    private AuditMessage given_an_auditMessage_without_any_attribute_set() throws ParseException {
        return AuditMessageBuilder.builder()
                .build();
    }

    private AuditMessage given_an_auditMessage_without_any_attribute_set_except_dateTime() throws ParseException {
        return AuditMessageBuilder.builder()
                .createdDateTime(format.parse(TIMESTAMP))
                .build();
    }

    private AuditMessage given_an_auditMessage_with_all_attributes_set() throws ParseException {
        return AuditMessageBuilder.builder()
                .process(ALARM)
                .component(WEBSOCKET)
                .name(DMM)
                .type(RESPONSE)
                .direction(IN)
                .subject("subject")
                .subjectType(IMSI)
                .user("user")
                .transactionId("transactionId")
                .sequenceId("sequenceId")
                .status(SUCCESS)
                .byteSize(100)
                .payload(singletonMap("payload_key", "payload_value"))
                .createdDateTime(format.parse(TIMESTAMP))
                .version(1)
                .build();
    }

    private AuditMessageProto.AuditMessage given_an_auditMessageProto_with_all_attributes_set_to_default_value() {
        return AuditMessageProto.AuditMessage.newBuilder().build();
    }

    private AuditMessageProto.AuditMessage given_an_auditMessageProto_with_all_attributes_set_to_default_value_except_timestamp() throws ParseException {
        return AuditMessageProto.AuditMessage.newBuilder()
                .setCreatedDateTime(Timestamp.newBuilder()
                        .setSeconds(format.parse(TIMESTAMP).getTime() / 1000)
                        .setNanos((int) ((format.parse(TIMESTAMP).getTime() % 1000) * 1000000))
                        .build())
                .build();
    }
    private AuditMessageProto.AuditMessage given_an_auditMessageProto_with_all_attributes_set_to_other_than_default_value() throws ParseException {
        return AuditMessageProto.AuditMessage.newBuilder()
                .setProcess(AuditMessageProto.AuditMessage.ProcessType.ALARM)
                .setComponent(AuditMessageProto.AuditMessage.ComponentType.WEBSOCKET)
                .setName(AuditMessageProto.AuditMessage.NameType.DMM)
                .setType(AuditMessageProto.AuditMessage.MsgType.RESPONSE)
                .setDirection(AuditMessageProto.AuditMessage.MsgDirection.IN)
                .setSubject("subject")
                .setSubjectType(AuditMessageProto.AuditMessage.SubjectType.IMSI)
                .setUser("user")
                .setTransactionId("transactionId")
                .setSequenceId("sequenceId")
                .setStatus(AuditMessageProto.AuditMessage.MsgStatus.SUCCESS)
                .setByteSize(100)
                .putAllPayload(singletonMap("payload_key", "payload_value"))
                .setCreatedDateTime(Timestamp.newBuilder()
                        .setSeconds(format.parse(TIMESTAMP).getTime() / 1000)
                        .setNanos((int) ((format.parse(TIMESTAMP).getTime() % 1000) * 1000000))
                        .build())
                .setVersion(1)
                .build();
    }

    private void verify_that_auditMessageProto_has_all_attributes_set(AuditMessageProto.AuditMessage auditMessageProto) throws ParseException {
        assertThat(auditMessageProto, notNullValue());
        assertThat(auditMessageProto.getProcess(), is(AuditMessageProto.AuditMessage.ProcessType.ALARM));
        assertThat(auditMessageProto.getComponent(), is(AuditMessageProto.AuditMessage.ComponentType.WEBSOCKET));
        assertThat(auditMessageProto.getName(), is(AuditMessageProto.AuditMessage.NameType.DMM));
        assertThat(auditMessageProto.getType(), is(AuditMessageProto.AuditMessage.MsgType.RESPONSE));
        assertThat(auditMessageProto.getDirection(), is(AuditMessageProto.AuditMessage.MsgDirection.IN));
        assertThat(auditMessageProto.getSubject(), is("subject"));
        assertThat(auditMessageProto.getSubjectType(), is(AuditMessageProto.AuditMessage.SubjectType.IMSI));
        assertThat(auditMessageProto.getUser(), is("user"));
        assertThat(auditMessageProto.getTransactionId(), is("transactionId"));
        assertThat(auditMessageProto.getSequenceId(), is("sequenceId"));
        assertThat(auditMessageProto.getStatus(), is(AuditMessageProto.AuditMessage.MsgStatus.SUCCESS));
        assertThat(auditMessageProto.getByteSize(), is(100));
        assertThat(auditMessageProto.getPayloadCount(), is(1));
        assertThat(auditMessageProto.getPayloadMap(), hasEntry("payload_key", "payload_value"));
        assertThat(auditMessageProto.getCreatedDateTime().getSeconds(), is(format.parse(TIMESTAMP).getTime() / 1000));
        assertThat(auditMessageProto.getCreatedDateTime().getNanos(), is((int) (format.parse(TIMESTAMP).getTime() % 1000) * 1000000));
        assertThat(auditMessageProto.getVersion(), is(1));
    }

    private void verify_that_auditMessageProto_has_all_attributes_set_to_default(AuditMessageProto.AuditMessage auditMessageProto) throws ParseException {
        assertThat(auditMessageProto, notNullValue());
        assertThat(auditMessageProto.getProcess(), is(AuditMessageProto.AuditMessage.ProcessType.NO_PROCESS));
        assertThat(auditMessageProto.getComponent(), is(AuditMessageProto.AuditMessage.ComponentType.NO_COMPONENT));
        assertThat(auditMessageProto.getName(), is(AuditMessageProto.AuditMessage.NameType.NO_NAME));
        assertThat(auditMessageProto.getType(), is(AuditMessageProto.AuditMessage.MsgType.NO_TYPE));
        assertThat(auditMessageProto.getDirection(), is(AuditMessageProto.AuditMessage.MsgDirection.NO_DIR));
        assertThat(auditMessageProto.getSubject(), is(""));
        assertThat(auditMessageProto.getSubjectType(), is(AuditMessageProto.AuditMessage.SubjectType.NO_SUBJECT));
        assertThat(auditMessageProto.getUser(), is(""));
        assertThat(auditMessageProto.getTransactionId(), is(""));
        assertThat(auditMessageProto.getSequenceId(), is(""));
        assertThat(auditMessageProto.getStatus(), is(AuditMessageProto.AuditMessage.MsgStatus.NONE));
        assertThat(auditMessageProto.getByteSize(), is(0));
        assertThat(auditMessageProto.getPayloadCount(), is(0));
        assertThat(auditMessageProto.getCreatedDateTime().getSeconds(), is(format.parse(TIMESTAMP).getTime() / 1000));
        assertThat(auditMessageProto.getCreatedDateTime().getNanos(), is((int) (format.parse(TIMESTAMP).getTime() % 1000) * 1000000));
        assertThat(auditMessageProto.getVersion(), is(0));
    }

    private void verify_that_auditMessage_has_all_attributes_set(AuditMessage auditMessage) throws ParseException {
        assertThat(auditMessage, notNullValue());
        assertThat(auditMessage.getProcess(), is(ALARM));
        assertThat(auditMessage.getComponent(), is(WEBSOCKET));
        assertThat(auditMessage.getName(), is(DMM));
        assertThat(auditMessage.getType(), is(RESPONSE));
        assertThat(auditMessage.getDirection(), is(IN));
        assertThat(auditMessage.getSubject(), is("subject"));
        assertThat(auditMessage.getSubjectType(), is(IMSI));
        assertThat(auditMessage.getUser(), is("user"));
        assertThat(auditMessage.getTransactionId(), is("transactionId"));
        assertThat(auditMessage.getSequenceId(), is("sequenceId"));
        assertThat(auditMessage.getStatus(), is(SUCCESS));
        assertThat(auditMessage.getByteSize(), is(100));
        assertThat(auditMessage.getPayload().size(), is(1));
        assertThat(auditMessage.getPayload(), hasEntry("payload_key", "payload_value"));
        assertThat(auditMessage.getCreatedDateTime(), is(format.parse(TIMESTAMP)));
        assertThat(auditMessage.getVersion(), is(1));
    }

    private void verify_that_auditMessage_has_all_attributes_null(AuditMessage auditMessage) throws ParseException {
        assertThat(auditMessage, notNullValue());
        assertThat(auditMessage.getProcess(), nullValue());
        assertThat(auditMessage.getComponent(), nullValue());
        assertThat(auditMessage.getName(), nullValue());
        assertThat(auditMessage.getType(), nullValue());
        assertThat(auditMessage.getDirection(), nullValue());
        assertThat(auditMessage.getSubject(), nullValue());
        assertThat(auditMessage.getSubjectType(), nullValue());
        assertThat(auditMessage.getUser(), nullValue());
        assertThat(auditMessage.getTransactionId(), nullValue());
        assertThat(auditMessage.getSequenceId(), nullValue());
        assertThat(auditMessage.getStatus(), nullValue());
        assertThat(auditMessage.getByteSize(), is(0));
        assertThat(auditMessage.getPayload().size(), is(0));
        assertThat(auditMessage.getCreatedDateTime(), is(format.parse(TIMESTAMP)));
        assertThat(auditMessage.getVersion(), is(0));
    }
}
