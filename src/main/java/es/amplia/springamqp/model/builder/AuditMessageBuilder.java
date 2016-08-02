package es.amplia.springamqp.model.builder;

import es.amplia.springamqp.model.AuditMessage;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AuditMessageBuilder {

    private AuditMessage auditMessage;

    protected AuditMessageBuilder() {
        auditMessage = instantiateConcreteMessage();
    }

    protected abstract AuditMessage instantiateConcreteMessage();

    protected AuditMessage getAuditMessage() {
        return auditMessage;
    }

    public final AuditMessage build () {
        return getAuditMessage();
    }

    public AuditMessageBuilder component(String component) {
        auditMessage.setComponent(component);
        return this;
    }

    public AuditMessageBuilder name(String name) {
        auditMessage.setName(name);
        return this;
    }

    public AuditMessageBuilder type(AuditMessage.MsgType type) {
        auditMessage.setType(type);
        return this;
    }

    public AuditMessageBuilder direction(AuditMessage.MsgDirection direction) {
        auditMessage.setDirection(direction);
        return this;
    }

    public AuditMessageBuilder subject(String subject) {
        auditMessage.setSubject(subject);
        return this;
    }

    public AuditMessageBuilder transactionId(String transactionId) {
        auditMessage.setTransactionId(transactionId);
        return this;
    }

    public AuditMessageBuilder status(AuditMessage.MsgStatus status) {
        auditMessage.setStatus(status);
        return this;
    }

    public AuditMessageBuilder byteSize(int byteSize) {
        auditMessage.setByteSize(byteSize);
        return this;
    }

    public AuditMessageBuilder payload(Map<String, String> payload) {
        checkNotNull(payload);
        auditMessage.setPayload(new HashMap<>(payload));
        return this;
    }
}
