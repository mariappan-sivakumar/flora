package com.mari.flora.mapper;

import com.mari.flora.dto.request.GarlandRequest;
import com.mari.flora.dto.response.GarlandResponse;
import com.mari.flora.entity.Garland;
import org.springframework.stereotype.Component;

@Component
public class GarlandMapper {

    public GarlandResponse toResponse(Garland garland) {
        if (garland == null) {
            return null;
        }

        return GarlandResponse.builder()
                .id(garland.getId())
                .name(garland.getName())
                .productCode(garland.getProductCode())
                .price(garland.getPrice())
                .description(garland.getDescription())
                .orderBy(garland.getOrderBy())
                .categoryId(garland.getCategory() != null ? garland.getCategory().getId() : null)
                .imageId(garland.getImage() != null ? garland.getImage().getImageId() : null)
                .isAvailable(garland.getIsAvailable())
                .isActive(garland.getIsActive())
                .createdAt(garland.getCreatedAt())
                .updatedAt(garland.getUpdatedAt())
                .materials(garland.getMaterials())
                .imageUrl(garland.getImage() != null ? garland.getImage().getImagePath() : null)
                .build();
    }

    public Garland toEntity(GarlandResponse response) {
        if (response == null) {
            return null;
        }

        return Garland.builder()
                .id(response.getId())
                .name(response.getName())
                .productCode(response.getProductCode())
                .price(response.getPrice())
                .description(response.getDescription())
                .orderBy(response.getOrderBy())
                .isAvailable(response.getIsAvailable())
                .isActive(response.getIsActive() != null ? response.getIsActive() : Boolean.TRUE)
                .build();
    }

    public Garland toEntity(GarlandRequest garland) {
        if (garland == null) {
            return null;
        }

        return Garland.builder()
                .name(garland.getName())
                .productCode(garland.getProductCode())
                .price(garland.getPrice())
                .description(garland.getDescription())
                .orderBy(garland.getOrderBy())
                .isAvailable(garland.getIsAvailable())
                .isActive(garland.getIsActive() != null ? garland.getIsActive() : Boolean.TRUE)
                .materials(garland.getMaterials())
                .build();
    }
}
