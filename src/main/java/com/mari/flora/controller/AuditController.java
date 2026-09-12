package com.mari.flora.controller;

import com.mari.flora.audit.AuditService;
import com.mari.flora.dto.response.AuditLogResponse;
import com.mari.flora.dto.response.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "Audit", description = "Administrative audit log APIs")
@Slf4j
public class AuditController {
    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @Operation(summary = "Get audit logs", description = "Retrieve paginated audit logs with optional filters")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Audit logs retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseDto<Page<AuditLogResponse>> getAuditLogs(@RequestParam(required = false) Long userId, @RequestParam(required = false) String action, @RequestParam(required = false) String entityType, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "created_at") String sortBy, @RequestParam(defaultValue = "desc") String sortDir) {
        log.info("getAuditLogs called userId={}, action={}, entityType={}, page={}, size={}, sortBy={}, sortDir={}", userId, action, entityType, page, size, sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<AuditLogResponse> auditLogs = auditService.getAuditLogs(userId, action, entityType, pageable);
        log.debug("getAuditLogs returned {} audit logs", auditLogs.getNumberOfElements());
        return ResponseDto.success(auditLogs, "Audit logs retrieved successfully");
    }
}
