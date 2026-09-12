package com.mari.flora.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Company profile details returned to clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyResponse {

    @Schema(description = "Company ID", example = "1")
    private Integer id;
    @Schema(description = "Company name", example = "Flora Atelier")
    private String name;
    @Schema(description = "Long company description", example = "Luxury floral brand specializing in handcrafted arrangements.")
    private String description;
    @Schema(description = "Short company summary", example = "Luxury floral artistry")
    private String shortDescription;
    @Schema(description = "Associated image record ID", example = "42")
    private Integer imageId;
    @Schema(description = "Company physical address", example = "12 Garden Street, Chennai")
    private String address;
    @Schema(description = "Primary phone number", example = "+919876543210")
    private String phoneNumber;
    @Schema(description = "Company email address", example = "hello@flora.com")
    private String emailId;
    @Schema(description = "WhatsApp contact number", example = "+919876543210")
    private String whatsapp;
    @Schema(description = "Whether the company is currently active", example = "true")
    private Boolean isActive;
    @Schema(description = "Creation timestamp", example = "2026-09-12T19:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Last update timestamp", example = "2026-09-12T19:15:00")
    private LocalDateTime updatedAt;
    @Schema(description = "Hero title shown on the page", example = "Fresh Flowers for Every Celebration")
    private String heroTitle;
    @Schema(description = "KPI list for the company profile")
    private List<CompanyKpiResponse> kpis;
    @Schema(description = "Public image URL for the company profile image", example = "https://cdn.example.com/company.jpg")
    private String imageUrl;
}
