package com.mari.flora.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Schema(description = "User summary returned to clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    @Schema(description = "User ID", example = "12")
    private Long id;
    @Schema(description = "Username for the account", example = "admin")
    private String username;
    @Schema(description = "Primary email address", example = "admin@flora.com")
    private String email;
    @Schema(description = "Whether the user account is active", example = "true")
    private Boolean isActive;
    @Schema(description = "Roles assigned to the user", example = "[\"ROLE_ADMIN\"]")
    private Set<String> roles;
}
