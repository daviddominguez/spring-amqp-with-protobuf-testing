package es.amplia.springamqp.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import es.amplia.springamqp.model.serializer.ProtobufSerializer;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class ProtobufMessageConverter<T> extends AbstractMessageConverter {

    private final static String MESSAGE_DESCRIPTOR_NAME = "protobuf_descriptor_name";
    public final static String CONTENT_TYPE_PROTOBUF = "application/protobuf";

    private Descriptors.FileDescriptor fileDescriptor;
    private ProtobufSerializer<T> serializer;

    public ProtobufMessageConverter(Descriptors.FileDescriptor fileDescriptor, ProtobufSerializer<T> serializer) {
        this.fileDescriptor = fileDescriptor;
        this.serializer = serializer;
    }

    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) {
        checkNotNull(object, "Object to send is null");

        com.google.protobuf.Message protobuf = serializer.serialize((T) object);
        if (!com.google.protobuf.Message.class.isAssignableFrom(protobuf.getClass())) {
            throw new MessageConversionException("Message wasn't a protobuf");
        } else {
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
                Descriptors.Descriptor messageType = fileDescriptor.findMessageTypeByName(descriptorName);
                return serializer.deserialize(DynamicMessage.parseFrom(messageType, message.getBody()));
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
        return checkNotNull(headers.get(ProtobufMessageConverter.MESSAGE_DESCRIPTOR_NAME),
                "%s header not found in MessageProperties", ProtobufMessageConverter.MESSAGE_DESCRIPTOR_NAME).toString();
    }
}