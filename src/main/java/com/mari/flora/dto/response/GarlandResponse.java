package com.mari.flora.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GarlandResponse {

    private Integer id;
    private String name;
    private String productCode;
    private BigDecimal price;
    private String description;
    private Integer orderBy;
    private Integer categoryId;
    private Integer imageId;
    private Boolean isAvailable;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> materials;
    private String imageUrl;
}
