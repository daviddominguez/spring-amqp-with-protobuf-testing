package es.amplia.springamqp.model.serializer;

import com.google.protobuf.Message;

public interface ProtobufSerializer<T> {

    Message serialize(T object);
    T deserialize(Message message);
}
