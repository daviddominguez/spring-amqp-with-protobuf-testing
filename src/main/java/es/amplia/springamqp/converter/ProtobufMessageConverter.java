package es.amplia.springamqp.converter;

import es.amplia.springamqp.model.serializer.ProtobufSerializer;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public class ProtobufMessageConverter<T> extends AbstractMessageConverter {

    private final static String PROTOBUF_CONTENT_TYPE = "application/protobuf";

    private ProtobufSerializer<T> serializer;

    public ProtobufMessageConverter(ProtobufSerializer<T> serializer) {
        this.serializer = serializer;
    }

    @Override
    protected Message createMessage(Object object, MessageProperties messageProperties) {
        checkNotNull(object, "Object to send is null");

        byte[] byteArray = serializer.serialize((T) object);

        messageProperties.setContentLength(byteArray.length);
        messageProperties.setContentType(PROTOBUF_CONTENT_TYPE);

        return new Message(byteArray, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        try {
            String contentType = message.getMessageProperties().getContentType();
            if (PROTOBUF_CONTENT_TYPE.equals(contentType)) {
                return serializer.deserialize(message.getBody());
            }
            else {
                throw new IllegalArgumentException(format("Incorrect content-type message %s", contentType));
            }
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("Cannot convert message", e);
        }
    }
}