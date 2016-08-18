package es.amplia.springamqp.model;

import com.google.common.base.Objects;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;

public class AuditMessage {

    public enum ProcessType {CONNECTOR, REST_NORTH, ALARM}
    public enum ComponentType {RACO, PING_RELAY, REST, WEBSOCKET, MQTT, WS_JOB, WS_PROVISION, SNMP}
    public enum NameType {ACCT_START, PING_REQUEST, PING_REPLY, DMM, OPERATION, ACCESS_VALIDATION, REFRESH_PRESENCE, CHANGE_PRESENCE, DEVICES}
    public enum MsgType {EVENT, REQUEST, RESPONSE, CONNECTION, INSERT, UPDATE, DELETE, CALLBACK, NOTIFICATION}
    public enum MsgDirection {IN, OUT}
    public enum SubjectType {IMSI, DEVICE, JOB, IP}
    public enum MsgStatus {SUCCESS, ERROR}

    private ProcessType process;
    private ComponentType component;
    private NameType name;
    private MsgType type;
    private MsgDirection direction;
    private String subject;
    private SubjectType subjectType;
    private String user;
    private String transactionId;
    private String sequenceId;
    private MsgStatus status;
    private int byteSize;
    private Map<String, String> payload;
    private Date createdDateTime;
    private int version;

    public ProcessType getProcess() {
        return process;
    }

    public void setProcess(ProcessType process) {
        this.process = process;
    }

    public ComponentType getComponent() {
        return component;
    }

    public void setComponent(ComponentType component) {
        this.component = component;
    }

    public NameType getName() {
        return name;
    }

    public void setName(NameType name) {
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

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(SubjectType subjectType) {
        this.subjectType = subjectType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
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

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditMessage)) return false;
        AuditMessage that = (AuditMessage) o;
        return equal(byteSize, that.byteSize) &&
                equal(version, that.version) &&
                equal(process, that.process) &&
                equal(component, that.component) &&
                equal(name, that.name) &&
                equal(type, that.type) &&
                equal(direction, that.direction) &&
                equal(subject, that.subject) &&
                equal(subjectType, that.subjectType) &&
                equal(user, that.user) &&
                equal(transactionId, that.transactionId) &&
                equal(sequenceId, that.sequenceId) &&
                equal(status, that.status) &&
                equal(payload, that.payload) &&
                equal(createdDateTime, that.createdDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(process, component, name, type, direction, subject, subjectType, user, transactionId,
                sequenceId, status, byteSize, payload, createdDateTime, version);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("process", process)
                .add("component", component)
                .add("name", name)
                .add("type", type)
                .add("direction", direction)
                .add("subject", subject)
                .add("subjectType", subjectType)
                .add("user", user)
                .add("transactionId", transactionId)
                .add("sequenceId", sequenceId)
                .add("status", status)
                .add("byteSize", byteSize)
                .add("payload", payload)
                .add("createdDateTime", createdDateTime)
                .add("version", version)
                .omitNullValues()
                .toString();
    }
}
