package com.mari.flora.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditEventListener {

    private final AuditService auditService;

    // ✅ Fires ONLY after the originating business transaction commits successfully.
    // ✅ @Async moves it off the caller's thread entirely.
    @Async("auditExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAuditEvent(AuditEvent event) {
        try {
            auditService.persist(event);
        } catch (Exception ex) {
            // Audit failures must NEVER surface to the caller — business op already committed.
            log.error("Failed to persist audit log for entityType={}, entityId={}, action={}",
                    event.getEntityType(), event.getEntityId(), event.getAction(), ex);
        }
    }
}