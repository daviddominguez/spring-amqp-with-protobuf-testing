package es.amplia.springamqp;

import es.amplia.springamqp.model.AuditMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    public static final String AUDIT_MESSAGE_METHOD_NAME = "processMessage";

    public void processMessage(AuditMessage message) {
        LOGGER.debug("Audit message received '{}'", message);
    }
}