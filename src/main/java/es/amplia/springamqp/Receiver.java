package es.amplia.springamqp;

import es.amplia.springamqp.model.AuditMessageNorth;
import es.amplia.springamqp.model.AuditMessageSouth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    public static final String NORTH_MSG_METHOD_NAME = "processNorthMessage";
    public static final String SOUTH_MSG_METHOD_NAME = "processSouthMessage";

    public void processNorthMessage(AuditMessageNorth message) {
        LOGGER.debug("North message received '{}'", message);
    }

    public void processSouthMessage(AuditMessageSouth message) {
        LOGGER.debug("South message received '{}'", message);
    }
}