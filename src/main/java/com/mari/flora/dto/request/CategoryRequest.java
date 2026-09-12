package com.mari.flora.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Payload for creating or updating a product category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    @Schema(description = "Category display name", example = "Wedding")
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Sort order used in category listings", example = "1")
    @Min(value = 0, message = "Order must be greater than or equal to 0")
    private Integer order;

    @Schema(description = "Category description shown in the UI", example = "Fresh floral arrangements for weddings")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Schema(description = "Image record ID associated with the category banner or thumbnail", example = "15")
    @Positive(message = "Image ID must be positive")
    private Long imageId;

    @Schema(description = "Whether the category is active and visible to customers", example = "true")
    private Boolean isActive;

}
