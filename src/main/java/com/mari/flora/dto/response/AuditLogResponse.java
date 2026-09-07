package com.mari.flora.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {

    private Long id;
    private Long userId;
    private String username;
    private String action;
    private String entityType;
    private Long entityId;
    private String message;
    private String timestamp;

}
