package com.mari.flora.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarlandRequest {

    @NotBlank(message = "Garland name is required")
    @Size(min = 2, max = 150, message = "Garland name must be between 2 and 150 characters")
    private String name;

    @NotBlank(message = "Product code is required")
    @Size(min = 5, max = 50, message = "Product code must be between 5 and 50 characters")
    private String productCode;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Price must not exceed 999999.99")
    private BigDecimal price;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Min(value = 0, message = "Order by must be greater than or equal to 0")
    private Integer orderBy;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;

    @Positive(message = "Image ID must be positive")
    private Long imageId;

    private Boolean isAvailable;

    private Boolean isActive;

    private List<String> materials;
}
