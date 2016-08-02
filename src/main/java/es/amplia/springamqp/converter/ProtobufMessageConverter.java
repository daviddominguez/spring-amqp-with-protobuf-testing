package es.amplia.springamqp.converter;

import es.amplia.springamqp.protobuf.AuditMessageProtobuf;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class ProtobufMessageConverter extends AbstractMessageConverter {

    private final static String MESSAGE_DESCRIPTOR_NAME = "protobuf_descriptor_name";
    public final static String CONTENT_TYPE_PROTOBUF = "application/protobuf";

    public ProtobufMessageConverter() {
    }

    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) {
        checkNotNull(object, "Object to send is null");

        if (!com.google.protobuf.Message.class.isAssignableFrom(object.getClass())) {
            throw new MessageConversionException("Message wasn't a protobuf");
        } else {
            com.google.protobuf.Message protobuf = (com.google.protobuf.Message) object;
            byte[] byteArray = protobuf.toByteArray();

            messageProperties.setContentLength(byteArray.length);
            messageProperties.setContentType(ProtobufMessageConverter.CONTENT_TYPE_PROTOBUF);
            messageProperties.setHeader(ProtobufMessageConverter.MESSAGE_DESCRIPTOR_NAME, protobuf.getDescriptorForType().getName());

            return new Message(byteArray, messageProperties);
        }
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        try {
            if (ProtobufMessageConverter.CONTENT_TYPE_PROTOBUF.equals(message.getMessageProperties().getContentType())) {
                String descriptorName = getMessageDescriptorName(message);
                if (descriptorName.equals(AuditMessageProtobuf.AuditMessageNorthProtobuf.getDescriptor().getName()))
                    return AuditMessageProtobuf.AuditMessageNorthProtobuf.parseFrom(message.getBody());
                if (descriptorName.equals(AuditMessageProtobuf.AuditMessageSouthProtobuf.getDescriptor().getName()))
                    return AuditMessageProtobuf.AuditMessageSouthProtobuf.parseFrom(message.getBody());
                // Its an unexpected protobuf msg.
                throw new IllegalArgumentException();
            }
            // it isn't a protobuf
            else {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException(format("Cannot convert, unknown message type %s", getMessageDescriptorName(message)));
        }
    }

    private String getMessageDescriptorName(Message msg) {
        Map<String, Object> headers = msg.getMessageProperties().getHeaders();
        return checkNotNull(headers.get(ProtobufMessageConverter.MESSAGE_DESCRIPTOR_NAME)).toString();
    }
}