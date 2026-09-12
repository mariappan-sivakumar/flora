package com.mari.flora.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Login response including token and user summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
    @Schema(description = "Refresh token for reissuing authentication", example = "refresh-token-value")
    private String refreshToken;
    @Schema(description = "Authenticated user summary")
    private UserResponse user;
    @Schema(description = "Token lifetime in seconds", example = "3600")
    private Long expiresIn;
}
