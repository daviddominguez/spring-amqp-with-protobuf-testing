package es.amplia.springamqp.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "audit.amqp")
public class AmqpProperties {

    private Queue queue;

    public static class Queue {
        private String auditMessageNorth;
        private String auditMessageSouth;

        public String getAuditMessageNorth() {
            return auditMessageNorth;
        }

        public void setAuditMessageNorth(String auditMessageNorth) {
            this.auditMessageNorth = auditMessageNorth;
        }

        public String getAuditMessageSouth() {
            return auditMessageSouth;
        }

        public void setAuditMessageSouth(String auditMessageSouth) {
            this.auditMessageSouth = auditMessageSouth;
        }
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}
