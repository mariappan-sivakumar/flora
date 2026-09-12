package com.mari.flora.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "KPI metadata for the company profile section")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyKpiResponse {
    @Schema(description = "KPI metric value such as a percentage or number", example = "20+")
    private String kpiValue;
    @Schema(description = "Display label for the KPI", example = "Countries We Export To")
    private String kpiLabel;
    // id, isActive, timestamps intentionally excluded — public-facing DTO only
}