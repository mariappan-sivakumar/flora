package com.mari.flora.mapper;

import com.mari.flora.dto.response.AuditLogResponse;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {
    public AuditLogResponse toResponse(com.mari.flora.entity.AuditLog auditLog) {
        if (auditLog == null) {
            return null;
        }

        return AuditLogResponse.builder()
                .id(auditLog.getId())
                .userId(auditLog.getUserId())
                .username(auditLog.getUsername())
                .action(auditLog.getAction().toString())
                .entityType(auditLog.getEntityType().toString())
                .entityId(auditLog.getEntityId())
                .message(auditLog.getMessage())
                .timestamp(auditLog.getCreatedAt().toString())
                .build();
    }
}
