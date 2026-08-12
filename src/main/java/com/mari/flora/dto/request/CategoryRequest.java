package com.mari.flora.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
    private String name;

    @Min(value = 0, message = "Order must be greater than or equal to 0")
    private Integer order;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Positive(message = "Image ID must be positive")
    private Long imageId;

    private Boolean isActive;

}
