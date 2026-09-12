package com.mari.flora.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Audit log record for administrative actions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

    @Schema(description = "Unique audit log ID", example = "101")
    private Long id;
    @Schema(description = "User ID associated with the action", example = "5")
    private Long userId;
    @Schema(description = "Username of the actor", example = "admin")
    private String username;
    @Schema(description = "Audit action performed", example = "CREATE")
    private String action;
    @Schema(description = "Entity type affected by the action", example = "GARLAND")
    private String entityType;
    @Schema(description = "Entity ID affected by the action", example = "42")
    private Long entityId;
    @Schema(description = "Human readable audit message", example = "Garland 'Rose Garland' created by admin")
    private String message;
    @Schema(description = "Audit timestamp", example = "2026-09-12T19:30:00Z")
    private String timestamp;

}
