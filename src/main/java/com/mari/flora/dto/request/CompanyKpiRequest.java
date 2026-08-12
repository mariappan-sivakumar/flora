package com.mari.flora.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyKpiRequest {

    @NotBlank(message = "KPI value is required")
    private String kpiValue;   // e.g. "20+", "100%", "Daily"

    @NotBlank(message = "KPI label is required")
    private String kpiLabel;   // e.g. "Countries We Export To"

    @NotNull(message = "Order is required")
    private Integer orderBy;
}