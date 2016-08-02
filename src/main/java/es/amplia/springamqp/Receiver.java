package es.amplia.springamqp;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

@SuppressWarnings("unused")
public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    public static final String DEFAULT_RECEIVE_METHOD_NAME = "processMessage";
    public static final String NORTH_MSG_METHOD_NAME = "processNorthMessage";
    public static final String SOUTH_MSG_METHOD_NAME = "processSouthMessage";

    /**
     * Default listener method
     * @param message
     * @see es.amplia.springamqp.configuration.SpringAmqpConfiguration#listenerAdapter
     */
    public void processMessage(Message message) {
        LOGGER.debug("Unknown message received '{}'", message);
    }

    public void processNorthMessage(Message message) {
        LOGGER.debug("North message received '{}'", message);
    }

    public void processSouthMessage(Message message) {
        LOGGER.debug("South message received '{}'", message);
    }
}