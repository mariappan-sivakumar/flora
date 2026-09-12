package com.mari.flora.audit;

import com.mari.flora.dto.response.AuditLogResponse;
import com.mari.flora.entity.AuditLog;
import com.mari.flora.mapper.AuditMapper;
import com.mari.flora.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditRepository auditLogRepository;
    private final AuditMapper auditMapper;

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

    public Page<AuditLogResponse> getAuditLogs(Long userId, String action, String entityType, Pageable pageable) {
        log.info("getAuditLogs called userId={}, action={}, entityType={}, page={}, size={}", userId, action, entityType, pageable.getPageNumber(), pageable.getPageSize());
        Page<AuditLog> auditLogs = auditLogRepository.findAll(userId, action, entityType, pageable);
        log.debug("getAuditLogs returned {} records", auditLogs.getTotalElements());
        return auditLogs.map(auditMapper::toResponse);
    }

}