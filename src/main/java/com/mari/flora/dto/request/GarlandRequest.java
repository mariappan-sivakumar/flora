package com.mari.flora.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Payload for creating or updating a garland product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarlandRequest {

    @Schema(description = "Garland title shown on the storefront", example = "Rose Garland")
    @NotBlank(message = "Garland name is required")
    @Size(min = 2, max = 150, message = "Garland name must be between 2 and 150 characters")
    private String name;

    @Schema(description = "Unique product code for the garland", example = "FL-ROSE-001")
    @NotBlank(message = "Product code is required")
    @Size(min = 5, max = 50, message = "Product code must be between 5 and 50 characters")
    private String productCode;

    @Schema(description = "Product price in the base currency", example = "499.00")
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price must not exceed 999999.99")
    private BigDecimal price;

    @Schema(description = "Detailed product description", example = "Premium handcrafted rose garland with long-lasting petals")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Schema(description = "Sorting order in category listing pages", example = "10")
    @Min(value = 0, message = "Order by must be greater than or equal to 0")
    private Integer orderBy;

    @Schema(description = "Category ID this garland belongs to", example = "3")
    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;

    @Schema(description = "Associated image record ID for the garland", example = "24")
    @Positive(message = "Image ID must be positive")
    private Long imageId;

    @Schema(description = "Whether the item is available for purchase", example = "true")
    private Boolean isAvailable;

    @Schema(description = "Whether the item is active in the system", example = "true")
    private Boolean isActive;

    @Schema(description = "Material list for the garland", example = "[\"rose\", \"lotus\"]")
    private List<String> materials;
}
