package com.mari.flora.audit;

import com.mari.flora.dto.enums.AuditAction;
import com.mari.flora.dto.enums.AuditEntityType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AuditEvent extends ApplicationEvent {

    private final Long userId;
    private final String username;
    private final AuditAction action;
    private final AuditEntityType entityType;
    private final Long entityId;
    private final String message;

    public AuditEvent(Object source, Long userId, String username, AuditAction action,
                      AuditEntityType entityType, Long entityId, String message) {
        super(source);
        this.userId = userId;
        this.username = username;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.message = message;
    }
}