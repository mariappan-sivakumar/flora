package com.mari.flora.service;

import com.mari.flora.dto.request.GarlandRequest;
import com.mari.flora.dto.response.GarlandBulkUploadResponse;
import com.mari.flora.dto.response.GarlandResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface GarlandService {
    Page<GarlandResponse> getAllGarlands(String search, String productCode, String category, int page, int size, String sortBy, String sortDirection);
    Page<GarlandResponse> getGarlandsByCategory(String categoryName, int page, int size, String sortBy, String sortDirection);
    GarlandResponse getGarlandByProductCode(String productCode);
    String createGarland(GarlandRequest garland);
    String updateGarland(GarlandRequest garland);
    String deleteGarlandByProductCode(String productCode);
    GarlandBulkUploadResponse bulkUploadGarlands(Long categoryId, MultipartFile file);
}
