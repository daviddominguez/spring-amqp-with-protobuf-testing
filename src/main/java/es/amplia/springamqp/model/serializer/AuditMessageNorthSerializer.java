package es.amplia.springamqp.model.serializer;

import com.google.protobuf.InvalidProtocolBufferException;
import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.AuditMessageNorth;
import es.amplia.springamqp.model.builder.AuditMessageNorthBuilder;
import es.amplia.springamqp.protobuf.AuditMessageProtobuf.AuditMessageNorthProtobuf;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class AuditMessageNorthSerializer implements ProtobufSerializer<AuditMessageNorth> {

    @Override
    public byte[] serialize(AuditMessageNorth object) {
        return AuditMessageNorthProtobuf.newBuilder()
                .setComponent(object.getComponent())
                .setName(object.getName())
                .setType(AuditMessageNorthProtobuf.MsgType.valueOf(object.getType().name()))
                .setDirection(AuditMessageNorthProtobuf.MsgDirection.valueOf(object.getDirection().name()))
                .setSubject(object.getSubject())
                .setTransactionId(object.getTransactionId())
                .setStatus(AuditMessageNorthProtobuf.MsgStatus.valueOf(object.getStatus().name()))
                .setByteSize(object.getByteSize())
                .putAllPayload(getSafePayload(object.getPayload()))
                .setUserId(object.getUserId().toString())
                .build().toByteArray();
    }

    @Override
    public AuditMessageNorth deserialize(byte[] message) {
        try {
            AuditMessageNorthProtobuf protobuf = AuditMessageNorthProtobuf.parseFrom(message);
            return (AuditMessageNorth) AuditMessageNorthBuilder.builder()
                    .userId(UUID.fromString(protobuf.getUserId()))
                    .component(protobuf.getComponent())
                    .name(protobuf.getName())
                    .type(AuditMessage.MsgType.valueOf(protobuf.getType().name()))
                    .direction(AuditMessage.MsgDirection.valueOf(protobuf.getDirection().name()))
                    .subject(protobuf.getSubject())
                    .transactionId(protobuf.getTransactionId())
                    .status(AuditMessage.MsgStatus.valueOf(protobuf.getStatus().name()))
                    .byteSize(protobuf.getByteSize())
                    .payload(getSafePayload(protobuf.getPayloadMap()))
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
        return payload != null ? payload : Collections.<String, String>emptyMap();
    }
}
