package com.mari.flora.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "JWT authentication response returned after successful login")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JWTAuthResponse {
    @Schema(description = "JWT token used for authenticated requests", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
    @Schema(description = "Authentication token type", example = "Bearer")
    private String tokenType = "Bearer";
}

