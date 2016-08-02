package es.amplia.springamqp.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.UUID;

import static com.google.common.base.Objects.equal;

public class AuditMessageNorth extends AuditMessage {

    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditMessageNorth)) return false;
        if (!super.equals(o)) return false;
        AuditMessageNorth that = (AuditMessageNorth) o;
        return equal(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), userId);
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
                .add("userId", userId)
                .toString();
    }
}
