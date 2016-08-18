package es.amplia.springamqp.sender;

import es.amplia.springamqp.model.AuditMessage;

public interface AuditMessagesService {

    void sendAuditMessage(AuditMessage message);
}
