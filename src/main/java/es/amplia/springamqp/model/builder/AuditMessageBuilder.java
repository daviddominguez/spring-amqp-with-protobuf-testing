package es.amplia.springamqp.model.builder;

import es.amplia.springamqp.model.AuditMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuditMessageBuilder {

    public static AuditMessageBuilder builder() {
        return new AuditMessageBuilder();
    }

    private final AuditMessage auditMessage = new AuditMessage();

    public final AuditMessage build () {
        return auditMessage;
    }

    public AuditMessageBuilder process(AuditMessage.ProcessType process) {
        auditMessage.setProcess(process);
        return this;
    }

    public AuditMessageBuilder component(AuditMessage.ComponentType component) {
        auditMessage.setComponent(component);
        return this;
    }

    public AuditMessageBuilder name(AuditMessage.NameType name) {
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

    public AuditMessageBuilder subjectType(AuditMessage.SubjectType subjectType) {
        auditMessage.setSubjectType(subjectType);
        return this;
    }

    public AuditMessageBuilder user(String user) {
        auditMessage.setUser(user);
        return this;
    }

    public AuditMessageBuilder transactionId(String transactionId) {
        auditMessage.setTransactionId(transactionId);
        return this;
    }

    public AuditMessageBuilder sequenceId(String sequenceId) {
        auditMessage.setSequenceId(sequenceId);
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

    public AuditMessageBuilder createdDateTime(Date createdDate) {
        auditMessage.setCreatedDateTime(createdDate);
        return this;
    }

    public AuditMessageBuilder version(int version) {
        auditMessage.setVersion(version);
        return this;
    }
}
