package com.mari.flora.controller;

import com.mari.flora.dto.request.CategoryRequest;
import com.mari.flora.dto.response.CategoryResponse;
import com.mari.flora.dto.response.ResponseDto;
import com.mari.flora.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/category")
@Tag(name = "Category", description = "APIs for managing categories")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get all categories", description = "Retrieve a paginated list of categories, optionally filtered by search term")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of categories returned"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<ResponseDto<Page<CategoryResponse>>> getAllCategories(@RequestParam(required = false) String search, @RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("getAllCategories called with search='{}', page={}, size={}", search, page, size);
        Page<CategoryResponse> result = categoryService.getAllCategories(search, page, size);
        log.debug("getAllCategories successful, result={}", result);
        return ResponseEntity.ok(ResponseDto.success(result, "Categories retrieved successfully"));

    }

    @Operation(summary = "Get a category", description = "Retrieve category details by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{name}")
    public ResponseEntity<ResponseDto<CategoryResponse>> getCategory(@PathVariable String name) {
        log.info("getCategory called with name='{}'", name);
        CategoryResponse categoryResponse = categoryService.getCategory(name);

        log.debug("getCategory found category: {}", categoryResponse);
        return ResponseEntity.ok(ResponseDto.success(categoryResponse, "Category retrieved successfully"));
    }

    @Operation(summary = "Create a category", description = "Create a new category (admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDto<String>> createCategory(@RequestBody CategoryRequest categoryRequest) {
        log.info("createCategory called with request={}", categoryRequest);
        String response = categoryService.createCategory(categoryRequest);
        log.debug("createCategory created: {}", response);
        return ResponseEntity.ok(ResponseDto.success(response, "Category created successfully"));
    }

    @Operation(summary = "Update a category", description = "Update an existing category (admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Category update accepted"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDto<String>> updateCategory(@RequestBody CategoryRequest categoryRequest) {
        log.info("updateCategory called with request={}", categoryRequest);

        String response = categoryService.updateCategory(categoryRequest);
        log.debug("updateCategory response: {}", response);
        return ResponseEntity.ok(ResponseDto.success(response, "Category updated successfully"));

    }

    @Operation(summary = "Delete a category", description = "Delete a category by name (admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category deleted"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDto<String>> deleteCategory(@PathVariable String name) {
        log.info("deleteCategory called with name='{}'", name);

        String response = categoryService.deleteCategory(name);
        log.debug("deleteCategory response: {}", response);
        return ResponseEntity.ok(ResponseDto.success(response, "Category deleted successfully"));

    }

}