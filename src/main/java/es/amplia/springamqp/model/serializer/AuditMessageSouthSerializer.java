package es.amplia.springamqp.model.serializer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.AuditMessageSouth;
import es.amplia.springamqp.model.builder.AuditMessageSouthBuilder;
import es.amplia.springamqp.protobuf.AuditMessageProtobuf.AuditMessageSouthProtobuf;

import static com.google.common.base.Preconditions.checkArgument;

public class AuditMessageSouthSerializer implements ProtobufSerializer<AuditMessageSouth> {

    @Override
    public Message serialize(AuditMessageSouth object) {
        return AuditMessageSouthProtobuf.newBuilder()
                .setComponent(object.getComponent())
                .setName(object.getName())
                .setType(AuditMessageSouthProtobuf.MsgType.valueOf(object.getType().name()))
                .setDirection(AuditMessageSouthProtobuf.MsgDirection.valueOf(object.getDirection().name()))
                .setSubject(object.getSubject())
                .setTransactionId(object.getTransactionId())
                .setStatus(AuditMessageSouthProtobuf.MsgStatus.valueOf(object.getStatus().name()))
                .setByteSize(object.getByteSize())
                .putAllPayload(object.getPayload())
                .setDeviceId(object.getDeviceId())
                .build();
    }

    @Override
    public AuditMessageSouth deserialize(Message message) {
        checkArgument(AuditMessageSouthProtobuf.getDescriptor().getName().equals(message.getDescriptorForType().getName()));
        try {
            AuditMessageSouthProtobuf protobuf = AuditMessageSouthProtobuf.parseFrom(message.toByteArray());
            return (AuditMessageSouth) AuditMessageSouthBuilder.builder()
                    .deviceId(protobuf.getDeviceId())
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
