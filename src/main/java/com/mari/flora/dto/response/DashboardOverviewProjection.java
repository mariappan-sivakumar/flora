package com.mari.flora.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public interface DashboardOverviewProjection {
    @Schema(description = "Total number of collections", example = "15")
    Long getTotalCollections();
    @Schema(description = "Number of collections added in the current month", example = "3")
    Long getCollectionsAddedThisMonth();
    @Schema(description = "Total number of garlands in the catalog", example = "120")
    Long getTotalGarlands();
    @Schema(description = "Number of categories that currently have garlands", example = "8")
    Long getCategoriesWithGarlands();
    @Schema(description = "Number of garlands currently available for sale", example = "90")
    Long getAvailableGarlands();
    @Schema(description = "Number of garlands currently unavailable", example = "30")
    Long getUnavailableGarlands();
    @Schema(description = "Availability label for the dashboard", example = "Healthy")
    String getAvailabilityDisplay();
    @Schema(description = "Company profile completeness percentage", example = "75.50")
    BigDecimal getCompanyProfilePct();
}
