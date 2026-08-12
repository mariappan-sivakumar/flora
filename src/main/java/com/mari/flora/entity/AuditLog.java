package com.mari.flora.entity;

import com.mari.flora.dto.enums.AuditAction;
import com.mari.flora.dto.enums.AuditEntityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", length = 150)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 20, nullable = false)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", length = 30, nullable = false)
    private AuditEntityType entityType;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}