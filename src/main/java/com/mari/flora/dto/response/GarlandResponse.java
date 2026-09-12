package com.mari.flora.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Garland product details returned to clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GarlandResponse {

    @Schema(description = "Garland ID", example = "12")
    private Integer id;
    @Schema(description = "Garland display name", example = "Rose Garland")
    private String name;
    @Schema(description = "Unique product code", example = "FL-ROSE-001")
    private String productCode;
    @Schema(description = "Selling price", example = "499.00")
    private BigDecimal price;
    @Schema(description = "Product description", example = "Premium handcrafted rose garland")
    private String description;
    @Schema(description = "Display order in the listing", example = "10")
    private Integer orderBy;
    @Schema(description = "Parent category ID", example = "3")
    private Integer categoryId;
    @Schema(description = "Image record ID associated with the garland", example = "24")
    private Integer imageId;
    @Schema(description = "Whether the garland is available for purchase", example = "true")
    private Boolean isAvailable;
    @Schema(description = "Whether the garland is active in the catalog", example = "true")
    private Boolean isActive;
    @Schema(description = "Creation timestamp", example = "2026-09-12T19:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Last update timestamp", example = "2026-09-12T19:20:00")
    private LocalDateTime updatedAt;
    @Schema(description = "Material list used in the garland", example = "[\"rose\", \"lotus\"]")
    private List<String> materials;
    @Schema(description = "Public image URL for the garland", example = "https://cdn.example.com/rose-garland.jpg")
    private String imageUrl;
}
