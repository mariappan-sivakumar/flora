package com.mari.flora.controller;

import com.mari.flora.audit.AuditService;
import com.mari.flora.dto.response.AuditLogResponse;
import com.mari.flora.dto.response.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audit")
public class AuditController {
    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDto<Page<AuditLogResponse>> getAuditLogs(@RequestParam(required = false) Long userId, @RequestParam(required = false) String action, @RequestParam(required = false) String entityType, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "created_at") String sortBy, @RequestParam(defaultValue = "desc") String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<AuditLogResponse> auditLogs = auditService.getAuditLogs(userId, action, entityType, pageable);
        return ResponseDto.success(auditLogs, "Audit logs retrieved successfully");
    }
}
