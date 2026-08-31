package com.mari.flora.service.impl;

import com.mari.flora.audit.Auditable;
import com.mari.flora.dto.enums.AuditAction;
import com.mari.flora.dto.enums.AuditEntityType;
import com.mari.flora.dto.request.CategoryRequest;
import com.mari.flora.dto.response.CategoryResponse;
import com.mari.flora.entity.Category;
import com.mari.flora.entity.Image;
import com.mari.flora.exception.FunctionalException;
import com.mari.flora.mapper.CategoryMapper;
import com.mari.flora.repository.CategoryRepository;
import com.mari.flora.repository.ImageRepository;
import com.mari.flora.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ImageRepository imageRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ImageRepository imageRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.imageRepository = imageRepository;
    }

    @Override
    public Page<CategoryResponse> getAllCategories(String search, int page, int size) {
        log.info("getAllCategories called with search='{}', page={}, size={}", search, page, size);
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        Page<CategoryResponse> result = categoryRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(categoryMapper::toResponse);
        log.debug("getAllCategories returned {} items", result.getNumberOfElements());
        return result;
    }

    @Override
    public CategoryResponse getCategory(String name) {
        log.info("getCategory called with name='{}'", name);
        Category found = categoryRepository.findByName(name).orElseThrow(() -> new FunctionalException(HttpStatus.NOT_FOUND, "Category not found with name: " + name));
        CategoryResponse response = categoryMapper.toResponse(found);
        log.debug("getCategory result={}", response);
        return response;
    }

    @Override
    @Transactional
    @Auditable(
            action = AuditAction.CREATE,
            entityType = AuditEntityType.CATEGORY,
            condition = "#result == 'Category created successfully'",
            message = "Category '#{#categoryRequest.name}' created by #{#username}"
    )
    public String createCategory(CategoryRequest categoryRequest) {
        log.info("createCategory called with request name='{}'", categoryRequest.getName());
        Category category = categoryMapper.toEntity(categoryRequest);
        if (categoryRequest.getImageId() != null) {
            Image image = imageRepository.findById(categoryRequest.getImageId())
                    .orElseThrow(() -> new FunctionalException(HttpStatus.INTERNAL_SERVER_ERROR, "Image not found with ID: " + categoryRequest.getImageId()));
            category.setImage(image);
        }
        Category newCategory = categoryRepository.save(category);
        log.debug("createCategory created id={}", newCategory.getId());
        return "Category created successfully";
    }

    @Override
    @Transactional
    @Auditable(
            action = AuditAction.UPDATE,
            entityType = AuditEntityType.CATEGORY,
            condition = "#result == 'Category updated successfully'",
            message = "Category '#{#categoryRequest.name}' updated by #{#username}"
    )
    public String updateCategory(CategoryRequest categoryRequest) {
        log.info("updateCategory called for name='{}'", categoryRequest.getName());
        Category category = categoryRepository.findByName(categoryRequest.getName()).orElseThrow(() -> new FunctionalException(HttpStatus.NOT_FOUND, "Category not found with name: " + categoryRequest.getName()));
        category.setOrder(categoryRequest.getOrder());
        category.setDescription(categoryRequest.getDescription());
        category.setIsActive(categoryRequest.getIsActive());
        if (categoryRequest.getImageId() != null) {
            Image image = imageRepository.findById(categoryRequest.getImageId())
                    .orElseThrow(() -> new FunctionalException(HttpStatus.INTERNAL_SERVER_ERROR, "Image not found with ID: " + categoryRequest.getImageId()));
            category.setImage(image);
        }
        categoryRepository.save(category);
        log.debug("updateCategory updated name={}", categoryRequest.getName());
        return "Category updated successfully";
    }

    @Override
    @Transactional
    @Auditable(
            action = AuditAction.DELETE,
            entityType = AuditEntityType.CATEGORY,
            condition = "#result == 'Category deleted successfully'",
            message = "Category '#{#name}' deleted by #{#username}"
    )
    public String deleteCategory(String name) {
        log.info("deleteCategory called for name={}", name);
        int numberOfRecordDeleted = categoryRepository.softDeleteByName(name);
        if (numberOfRecordDeleted > 0) {
            log.debug("deleteCategory deleted count={}", numberOfRecordDeleted);
            return "Category deleted successfully";
        }
        log.warn("deleteCategory - category not found: {}", name);
        return "Category not found";
    }
}
