package com.mari.flora.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Admin registration payload used to create a new admin user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegistrationDto {
    @Schema(description = "Internal database ID of the admin user", example = "10")
    Long id;
    @Schema(description = "Admin email address used for login and notifications", example = "admin@flora.com")
    String email;
    @Schema(description = "Display name of the admin", example = "Flora Admin")
    String name;
    @Schema(description = "Primary contact number", example = "+919999999999")
    String contact;
    @Schema(description = "Role assigned to the user", example = "admin")
    String role;
    @Schema(description = "Secure password for the admin account", example = "StrongPass@123")
    String password;
}