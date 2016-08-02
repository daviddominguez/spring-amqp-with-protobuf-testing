package es.amplia.springamqp.model.builder;

import es.amplia.springamqp.model.AuditMessage;
import es.amplia.springamqp.model.AuditMessageSouth;

public class AuditMessageSouthBuilder extends AuditMessageBuilder {

    public static AuditMessageSouthBuilder builder() {
        return new AuditMessageSouthBuilder();
    }

    @Override
    protected AuditMessageSouth getAuditMessage() {
        return (AuditMessageSouth) super.getAuditMessage();
    }

    @Override
    protected AuditMessage instantiateConcreteMessage() {
        return new AuditMessageSouth();
    }

    public AuditMessageSouthBuilder deviceId(String deviceId) {
        getAuditMessage().setDeviceId(deviceId);
        return this;
    }
}
