package com.mari.flora.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Payload for creating or updating the active company profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequest {

    @Schema(description = "Company display name", example = "Flora Atelier")
    @Size(min = 2, max = 150, message = "Company name must be between 2 and 150 characters")
    private String name;

    @Schema(description = "Detailed company overview description", example = "Luxury floral brand specializing in handcrafted arrangements.")
    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Schema(description = "Short summary shown in hero or card sections", example = "Luxury floral artistry")
    @Size(max = 255, message = "Short description must not exceed 255 characters")
    private String shortDescription;

    @Schema(description = "Company address", example = "12 Garden Street, Chennai")
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Schema(description = "Company contact phone number", example = "+919876543210")
    @Pattern(regexp = "^[+]?[0-9]{1,15}$", message = "Phone number must be valid")
    private String phoneNumber;

    @Schema(description = "Official company email address", example = "hello@flora.com")
    @Email(message = "Email should be valid")
    private String emailId;

    @Schema(description = "WhatsApp support number", example = "+919876543210")
    @Pattern(regexp = "^[+]?[0-9]{1,15}$", message = "WhatsApp number must be valid")
    private String whatsapp;

    @Schema(description = "Image record ID used as company logo or cover image", example = "42")
    private Long imageId;

    @Schema(description = "Hero title shown in the landing section", example = "Fresh Flowers for Every Celebration")
    private String heroTitle;

    @Schema(description = "Company KPI entries displayed in the profile section")
    @Valid
    @Builder.Default
    private List<CompanyKpiRequest> kpis = new ArrayList<>();
}
