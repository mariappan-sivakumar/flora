package com.mari.flora.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Request payload for password reset initiation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForgetPasswordRequest {

    @Schema(description = "Email address associated with the account", example = "user@flora.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
}
