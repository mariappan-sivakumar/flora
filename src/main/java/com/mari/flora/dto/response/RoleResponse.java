package com.mari.flora.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "User role metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {

    @Schema(description = "Role ID", example = "1")
    private Integer id;
    @Schema(description = "Role name in the application", example = "ROLE_ADMIN")
    private String roleName;
}
