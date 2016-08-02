package es.amplia.springamqp.model;

import com.google.common.base.Objects;

import java.util.Map;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;

public class AuditMessage {

    public enum MsgType {EVENT, REQUEST, RESPONSE, OTHER}
    public enum MsgDirection {IN, OUT}
    public enum MsgStatus {SUCCESS, ERROR}

    private String component;
    private String name;
    private MsgType type;
    private MsgDirection direction;
    private String subject;
    private String transactionId;
    private MsgStatus status;
    private int byteSize;
    private Map<String, String> payload;

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MsgType getType() {
        return type;
    }

    public void setType(MsgType type) {
        this.type = type;
    }

    public MsgDirection getDirection() {
        return direction;
    }

    public void setDirection(MsgDirection direction) {
        this.direction = direction;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public MsgStatus getStatus() {
        return status;
    }

    public void setStatus(MsgStatus status) {
        this.status = status;
    }

    public int getByteSize() {
        return byteSize;
    }

    public void setByteSize(int byteSize) {
        this.byteSize = byteSize;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditMessage)) return false;
        AuditMessage that = (AuditMessage) o;
        return byteSize == that.byteSize &&
                equal(component, that.component) &&
                equal(name, that.name) &&
                equal(type, that.type) &&
                equal(direction, that.direction) &&
                equal(subject, that.subject) &&
                equal(transactionId, that.transactionId) &&
                equal(status, that.status) &&
                equal(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(component, name, type, direction, subject, transactionId, status, byteSize, payload);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("component", component)
                .add("name", name)
                .add("type", type)
                .add("direction", direction)
                .add("subject", subject)
                .add("transactionId", transactionId)
                .add("status", status)
                .add("byteSize", byteSize)
                .add("payload", payload)
                .omitNullValues()
                .toString();
    }
}
