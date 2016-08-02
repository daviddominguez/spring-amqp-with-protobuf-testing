package es.amplia.springamqp.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import static com.google.common.base.Objects.equal;

public class AuditMessageSouth extends AuditMessage {

    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditMessageSouth)) return false;
        if (!super.equals(o)) return false;
        AuditMessageSouth that = (AuditMessageSouth) o;
        return equal(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), deviceId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("component", getComponent())
                .add("name", getName())
                .add("type", getType())
                .add("direction", getDirection())
                .add("subject", getSubject())
                .add("status", getStatus())
                .add("byteSize", getByteSize())
                .add("payload", getPayload())
                .add("deviceId", deviceId)
                .toString();
    }
}
