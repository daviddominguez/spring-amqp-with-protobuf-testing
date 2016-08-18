package es.amplia.springamqp.model.serializer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.builder.AuditMessageBuilder;
import es.amplia.springamqp.protobuf.AuditMessageProto;
import es.amplia.springamqp.protobuf.AuditMessageProto.AuditMessage.*;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static es.amplia.springamqp.protobuf.AuditMessageProto.AuditMessage.ComponentType.NO_COMPONENT;
import static es.amplia.springamqp.protobuf.AuditMessageProto.AuditMessage.MsgDirection.NO_DIR;
import static es.amplia.springamqp.protobuf.AuditMessageProto.AuditMessage.MsgStatus.NONE;
import static es.amplia.springamqp.protobuf.AuditMessageProto.AuditMessage.MsgType.NO_TYPE;
import static es.amplia.springamqp.protobuf.AuditMessageProto.AuditMessage.NameType.NO_NAME;
import static es.amplia.springamqp.protobuf.AuditMessageProto.AuditMessage.ProcessType.NO_PROCESS;
import static es.amplia.springamqp.protobuf.AuditMessageProto.AuditMessage.SubjectType.NO_SUBJECT;
import static org.apache.commons.lang3.EnumUtils.getEnum;

public class AuditMessageSerializer implements ProtobufSerializer<AuditMessage> {

    @Override
    public byte[] serialize(AuditMessage object) {
        return AuditMessageProto.AuditMessage.newBuilder()
                .setProcess(fromNullable(getEnum(ProcessType.class, Objects.toString(object.getProcess()))).or(NO_PROCESS))
                .setComponent(fromNullable(getEnum(ComponentType.class, Objects.toString(object.getComponent()))).or(NO_COMPONENT))
                .setName(fromNullable(getEnum(NameType.class, Objects.toString(object.getName()))).or(NO_NAME))
                .setType(fromNullable(getEnum(MsgType.class, Objects.toString(object.getType()))).or(NO_TYPE))
                .setDirection(fromNullable(getEnum(MsgDirection.class, Objects.toString(object.getDirection()))).or(NO_DIR))
                .setSubject(fromNullable(object.getSubject()).or(""))
                .setSubjectType(fromNullable(getEnum(SubjectType.class, Objects.toString(object.getSubjectType()))).or(NO_SUBJECT))
                .setUser(fromNullable(object.getUser()).or(""))
                .setTransactionId(fromNullable(object.getTransactionId()).or(""))
                .setSequenceId(fromNullable(object.getSequenceId()).or(""))
                .setStatus(fromNullable(getEnum(MsgStatus.class, Objects.toString(object.getStatus()))).or(NONE))
                .setByteSize(object.getByteSize())
                .putAllPayload(getSafePayload(object.getPayload()))
                .setCreatedDateTime(getTimestampFromDate(object.getCreatedDateTime()))
                .setVersion(object.getVersion())
                .build().toByteArray();
    }

    @Override
    public AuditMessage deserialize(byte[] message) {
        try {
            AuditMessageProto.AuditMessage protoObject = AuditMessageProto.AuditMessage.parseFrom(message);
            return AuditMessageBuilder.builder()
                    .process(getEnum(AuditMessage.ProcessType.class, Objects.toString(protoObject.getProcess())))
                    .component(getEnum(AuditMessage.ComponentType.class, Objects.toString(protoObject.getComponent().name())))
                    .name(getEnum(AuditMessage.NameType.class, Objects.toString(protoObject.getName().name())))
                    .type(getEnum(AuditMessage.MsgType.class, Objects.toString(protoObject.getType().name())))
                    .direction(getEnum(AuditMessage.MsgDirection.class, Objects.toString(protoObject.getDirection().name())))
                    .subject(getValueOrNullIfEmpty(protoObject.getSubject()))
                    .subjectType(getEnum(AuditMessage.SubjectType.class, Objects.toString(protoObject.getSubjectType().name())))
                    .user(getValueOrNullIfEmpty(protoObject.getUser()))
                    .transactionId(getValueOrNullIfEmpty(protoObject.getTransactionId()))
                    .sequenceId(getValueOrNullIfEmpty(protoObject.getSequenceId()))
                    .status(getEnum(AuditMessage.MsgStatus.class, Objects.toString(protoObject.getStatus().name())))
                    .byteSize(protoObject.getByteSize())
                    .payload(getSafePayload(protoObject.getPayloadMap()))
                    .createdDateTime(getDateFromTimestamp(protoObject.getCreatedDateTime()))
                    .version(protoObject.getVersion())
                    .build();
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("Error parsing protobuf object", e);
        }
    }

    /**
     * Avoids NullPointerException when payload is null and Protobuf's putAll method invoked.
     * @param payload nullable map
     * @return filled payload map or empty Map if null payload passed.
     * @see Map#putAll(Map)
     */
    private Map<String, String> getSafePayload(Map<String, String> payload) {
        return fromNullable(payload).or(Collections.<String, String>emptyMap());
    }

    private String getValueOrNullIfEmpty (String input) {
        return "".equals(input) ? null : input;
    }

    private Timestamp getTimestampFromDate(Date date) {
        checkNotNull(date, "date cannot be null");
        return Timestamp.newBuilder()
                .setSeconds(date.getTime() / 1000)
                .setNanos((int) ((date.getTime() % 1000) * 1000000)).build();
    }

    private Date getDateFromTimestamp(Timestamp ts) {
        checkArgument(ts.getSeconds() != 0 || ts.getNanos() != 0, "timestamp cannot be empty");
        return new Date((ts.getSeconds() * 1000) + (ts.getNanos() / 1000000));
    }
}
