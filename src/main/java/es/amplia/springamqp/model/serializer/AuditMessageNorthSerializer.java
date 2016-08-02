package es.amplia.springamqp.model.serializer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.AuditMessageNorth;
import es.amplia.springamqp.model.builder.AuditMessageNorthBuilder;
import es.amplia.springamqp.protobuf.AuditMessageProtobuf.AuditMessageNorthProtobuf;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

public class AuditMessageNorthSerializer implements ProtobufSerializer<AuditMessageNorth> {

    @Override
    public Message serialize(AuditMessageNorth object) {
        return AuditMessageNorthProtobuf.newBuilder()
                .setComponent(object.getComponent())
                .setName(object.getName())
                .setType(AuditMessageNorthProtobuf.MsgType.valueOf(object.getType().name()))
                .setDirection(AuditMessageNorthProtobuf.MsgDirection.valueOf(object.getDirection().name()))
                .setSubject(object.getSubject())
                .setTransactionId(object.getTransactionId())
                .setStatus(AuditMessageNorthProtobuf.MsgStatus.valueOf(object.getStatus().name()))
                .setByteSize(object.getByteSize())
                .putAllPayload(object.getPayload())
                .setUserId(object.getUserId().toString())
                .build();
    }

    @Override
    public AuditMessageNorth deserialize(Message message) {
        checkArgument(AuditMessageNorthProtobuf.getDescriptor().getName().equals(message.getDescriptorForType().getName()));
        try {
            AuditMessageNorthProtobuf protobuf = AuditMessageNorthProtobuf.parseFrom(message.toByteArray());
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
                    .payload(protobuf.getPayloadMap())
                    .build();
        } catch (InvalidProtocolBufferException e) {
            // TODO
            throw new RuntimeException();
        }
    }
}
