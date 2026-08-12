package com.mari.flora.audit;

import com.mari.flora.entity.AuditLog;
import com.mari.flora.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditRepository auditLogRepository;

    // ✅ REQUIRES_NEW: runs in its own transaction, independent of whatever
    // transaction (if any) is active on the async thread.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(AuditEvent event) {
        AuditLog logs = AuditLog.builder()
                .userId(event.getUserId())
                .username(event.getUsername())
                .action(event.getAction())
                .entityType(event.getEntityType())
                .entityId(event.getEntityId())
                .message(event.getMessage())
                .createdAt(OffsetDateTime.now())
                .build();

        auditLogRepository.save(logs);
        log.debug("Audit persisted: {} {} #{} by {}",
                event.getAction(), event.getEntityType(), event.getEntityId(), event.getUsername());
    }
}