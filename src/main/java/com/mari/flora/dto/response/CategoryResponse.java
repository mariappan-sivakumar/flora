package com.mari.flora.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "Category details returned to clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {

    @Schema(description = "Category ID", example = "3")
    private Integer id;
    @Schema(description = "Category name", example = "Wedding")
    private String name;
    @Schema(description = "Sort order within category list", example = "1")
    private Integer order;
    @Schema(description = "Category description", example = "Fresh floral arrangements for weddings")
    private String description;
    @Schema(description = "Associated image record ID", example = "15")
    private Integer imageId;
    @Schema(description = "Legacy sort field retained for compatibility", example = "name")
    private String orderBy;
    @Schema(description = "Whether the category is active", example = "true")
    private Boolean isActive;
    @Schema(description = "Creation timestamp", example = "2026-09-12T19:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Last update timestamp", example = "2026-09-12T19:15:00")
    private LocalDateTime updatedAt;
    @Schema(description = "Published image URL for the category", example = "https://cdn.example.com/category.jpg")
    private String imageUrl;
    @Schema(description = "Number of products under this category", example = "12")
    private Long numberOfItems;
}
