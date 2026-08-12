package com.mari.flora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyKpiResponse {
    private String kpiValue;
    private String kpiLabel;
    // id, isActive, timestamps intentionally excluded — public-facing DTO only
}