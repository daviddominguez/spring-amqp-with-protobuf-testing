package es.amplia.springamqp.model.builder;

import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.AuditMessageNorth;

import java.util.UUID;

public class AuditMessageNorthBuilder extends AuditMessageBuilder {

    public static AuditMessageNorthBuilder builder() {
        return new AuditMessageNorthBuilder();
    }

    @Override
    protected AuditMessageNorth getAuditMessage() {
        return (AuditMessageNorth) super.getAuditMessage();
    }

    @Override
    protected AuditMessage instantiateConcreteMessage() {
        return new AuditMessageNorth();
    }

    public AuditMessageNorthBuilder userId(UUID userId) {
        getAuditMessage().setUserId(userId);
        return this;
    }
}
