package es.amplia.springamqp.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "audit.amqp")
public class AmqpProperties {

    private Queue queue;

    public static class Queue {
        private String auditMessage;

        public String getAuditMessage() {
            return auditMessage;
        }

        public void setAuditMessage(String auditMessage) {
            this.auditMessage = auditMessage;
        }
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}
