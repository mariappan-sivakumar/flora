package com.mari.flora.service;

import com.mari.flora.dto.request.CategoryRequest;
import com.mari.flora.dto.response.CategoryResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {
    Page<CategoryResponse> getAllCategories(String search, int page, int size);
    CategoryResponse getCategory(String name);
    String createCategory(CategoryRequest categoryRequest);
    String updateCategory(CategoryRequest categoryRequest);
    String deleteCategory(String name);
}
