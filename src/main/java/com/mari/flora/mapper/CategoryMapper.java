package com.mari.flora.mapper;

import com.mari.flora.dto.request.CategoryRequest;
import com.mari.flora.dto.response.CategoryResponse;
import com.mari.flora.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .order(category.getOrder())
                .description(category.getDescription())
                .imageId(category.getImage() != null ? category.getImage().getImageId() : null)
                .isActive(category.getIsActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .imageUrl(category.getImage() != null ? category.getImage().getImagePath() : null)
                .build();
    }

    public Category toEntity(CategoryResponse response) {
        if (response == null) {
            return null;
        }

        return Category.builder()
                .id(response.getId())
                .name(response.getName())
                .order(response.getOrder())
                .description(response.getDescription())
                .isActive(response.getIsActive())
                .build();
    }

    public Category toEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }

        return Category.builder()
                .name(request.getName())
                .order(request.getOrder())
                .description(request.getDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : Boolean.TRUE)
                .build();
    }
}
