package es.amplia.springamqp.model.serializer;

public interface ProtobufSerializer<T> {

    byte[] serialize(T object);
    T deserialize(byte[] message);
}
