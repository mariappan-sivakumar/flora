package com.mari.flora.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Schema(description = "Single row parsed from a bulk CSV file before validation and persistence")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GarlandCsvRow {
    @Schema(description = "1-based row number in the source CSV, excluding the header", example = "2")
    private int rowNumber;      // 1-based, accounts for header row
    @Schema(description = "Garland name from the row", example = "Rose Garland")
    private String name;
    @Schema(description = "Description field from the row", example = "Classic rose arrangement")
    private String description;
    @Schema(description = "Sort order value from the row", example = "10")
    private String orderBy;
    @Schema(description = "Price value as a string before conversion", example = "499.00")
    private String price;
    @Schema(description = "Unique product code from the row", example = "FL-ROSE-001")
    private String productCode;
    @Schema(description = "Image URL from the row", example = "https://example.com/rose.jpg")
    private String imageUrl;
    @Schema(description = "Image file extension", example = "jpg")
    private String imageExtension;
    @Schema(description = "Material field as a pipe-separated string", example = "rose|lotus")
    private String material;    // pipe-separated, e.g. "rose|lotus"
}