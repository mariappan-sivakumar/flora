package com.mari.flora.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "KPI metadata for the company profile section")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyKpiRequest {

    @Schema(description = "KPI metric value such as a percentage or number", example = "20+")
    @NotBlank(message = "KPI value is required")
    private String kpiValue;   // e.g. "20+", "100%", "Daily"

    @Schema(description = "Human readable KPI label", example = "Countries We Export To")
    @NotBlank(message = "KPI label is required")
    private String kpiLabel;   // e.g. "Countries We Export To"

    @Schema(description = "Display ordering for the KPI within the list", example = "1")
    @NotNull(message = "Order is required")
    private Integer orderBy;
}