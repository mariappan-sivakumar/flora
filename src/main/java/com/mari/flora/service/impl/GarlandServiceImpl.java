package com.mari.flora.service.impl;

import com.mari.flora.audit.Auditable;
import com.mari.flora.dto.enums.AuditAction;
import com.mari.flora.dto.enums.AuditEntityType;
import com.mari.flora.dto.enums.GarlandRowStatus;
import com.mari.flora.dto.request.GarlandCsvRow;
import com.mari.flora.dto.request.GarlandRequest;
import com.mari.flora.dto.response.GarlandBulkUploadItemResult;
import com.mari.flora.dto.response.GarlandBulkUploadResponse;
import com.mari.flora.dto.response.GarlandResponse;
import com.mari.flora.entity.Category;
import com.mari.flora.entity.Garland;
import com.mari.flora.entity.Image;
import com.mari.flora.exception.FunctionalException;
import com.mari.flora.exception.GarlandRowProcessingException;
import com.mari.flora.mapper.GarlandMapper;
import com.mari.flora.repository.CategoryRepository;
import com.mari.flora.repository.GarlandRepository;
import com.mari.flora.repository.ImageRepository;
import com.mari.flora.service.GarlandService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class GarlandServiceImpl implements GarlandService {
    private final GarlandRepository garlandRepository;
    private final GarlandMapper garlandMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final GarlandRowProcessor garlandRowProcessor;
    private static final Set<String> REQUIRED_HEADERS = Set.of(
            "name", "price", "product_code", "image_url", "image_extension"
    );

    public GarlandServiceImpl(GarlandRepository garlandRepository, GarlandMapper garlandMapper, CategoryRepository categoryRepository, ImageRepository imageRepository, GarlandRowProcessor garlandRowProcessor) {
        this.garlandRepository = garlandRepository;
        this.garlandMapper = garlandMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.garlandRowProcessor = garlandRowProcessor;
    }

    @Override
    public Page<GarlandResponse> getAllGarlands(String search, String productCode, String category, int page, int size, String sortBy, String sortDirection) {
        log.info("getAllGarlands called search={}, productCode={}, category={}, page={}, size={}, sortBy={}, sortDirection={}", search, productCode, category, page, size, sortBy, sortDirection);

        Pageable pageable = getPageable(page, size, sortBy, sortDirection);
        Page<GarlandResponse> result = garlandRepository
                .searchGarlands(search, productCode, category, pageable)
                .map(garlandMapper::toResponse);
        log.debug("getAllGarlands returned {} items", result.getNumberOfElements());
        return result;

    }

    @Override
    public Page<GarlandResponse> getGarlandsByCategory(String categoryName, int page, int size, String sortBy, String sortDirection) {
        log.info("getGarlandsByCategory called category={}, page={}, size={}, sortBy={}, sortDirection={}", categoryName, page, size, sortBy, sortDirection);
        Pageable pageable = getPageable(page, size, sortBy, sortDirection);
        Page<GarlandResponse> result = garlandRepository
                .findByCategoryName(categoryName, pageable)
                .map(garlandMapper::toResponse);
        log.debug("getGarlandsByCategory returned {} items", result.getNumberOfElements());
        return result;
    }



    @Override
    public GarlandResponse getGarlandByProductCode(String productCode) {
        log.info("getGarlandByProductCode called productCode={}", productCode);
        GarlandResponse response = garlandRepository.findByProductCode(productCode)
                .map(garlandMapper::toResponse)
                .orElseThrow(() -> new FunctionalException(HttpStatus.NOT_FOUND, "Garland not found"));
        log.debug("getGarlandByProductCode result={}", response);
        return response;
    }

    @Override
    @Transactional
    @Auditable(
            action = AuditAction.CREATE,
            entityType = AuditEntityType.GARLAND,
            condition = "#result == 'Garland created successfully'",
            message = "Garland '#{#garland.name}' (Code: #{#garland.productCode}) added under category #{#garland.categoryId} by #{#username}"
    )
    public String createGarland(GarlandRequest garland) {
        log.info("createGarland called name={}, productCode={}", garland.getName(), garland.getProductCode());
        Garland newGarland = garlandMapper.toEntity(garland);
        Category category = categoryRepository.findById(garland.getCategoryId()).orElseThrow(() -> new FunctionalException(HttpStatus.NOT_FOUND, "Category not found"));
        newGarland.setCategory(category);
        if (garland.getImageId() != null) {
            Image image = imageRepository.findById(garland.getImageId()).orElseThrow(() -> new FunctionalException(HttpStatus.NOT_FOUND, "Image not found"));
            newGarland.setImage(image);
        }
        Garland savedGarland = garlandRepository.save(newGarland);
        log.debug("createGarland created id={}", savedGarland.getId());
        return "Garland created successfully";
    }

    @Override
    @Transactional
    @Auditable(
            action = AuditAction.UPDATE,
            entityType = AuditEntityType.GARLAND,
            condition = "#result == 'Garland updated successfully'",
            message = "Garland '#{#garland.name}' (Code: #{#garland.productCode}) updated by #{#username}"
    )

    public String updateGarland(GarlandRequest garland) {
        log.info("updateGarland called productCode={}", garland.getProductCode());
        Garland existingGarland = garlandRepository.findByProductCode(garland.getProductCode()).orElse(null);
        if (existingGarland != null) {
            existingGarland.setName(garland.getName());
            existingGarland.setPrice(garland.getPrice());
            existingGarland.setDescription(garland.getDescription());
            existingGarland.setOrderBy(garland.getOrderBy());
            existingGarland.setIsAvailable(garland.getIsAvailable());
            existingGarland.setIsActive(garland.getIsActive() != null ? garland.getIsActive() : Boolean.TRUE);
            garlandRepository.save(existingGarland);
            log.debug("updateGarland updated productCode={}", garland.getProductCode());
            return "Garland updated successfully";
        }
        log.warn("updateGarland - garland not found: {}", garland.getProductCode());
        return "Garland not found";
    }

    @Transactional
    @Auditable(
            action = AuditAction.DELETE,
            entityType = AuditEntityType.GARLAND,
            condition = "`#result == `'Garland deleted successfully'",
            message = "Garland (Code: #{#productCode}) deleted by #{#username}"
    )
    public String deleteGarlandByProductCode(String productCode) {
        log.info("deleteGarlandByProductCode called productCode={}", productCode);
        int numberOfRecordDeleted = garlandRepository.softDeleteByProductCode(productCode);
        if (numberOfRecordDeleted > 0) {
            log.debug("deleteGarlandByProductCode deleted count={}", numberOfRecordDeleted);
            return "Garland deleted successfully";
        }
        log.warn("deleteGarlandByProductCode - garland not found: {}", productCode);
        return "Garland not found";
    }

    private static @NonNull Pageable getPageable(int page, int size, String sortBy, String sortDirection) {
        String resolvedSortBy = (sortBy != null && !sortBy.isBlank()) ? sortBy : "name";
        Sort sort = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.by(resolvedSortBy).descending()
                : Sort.by(resolvedSortBy).ascending();
        return PageRequest.of(page, size, sort);
    }

    public GarlandBulkUploadResponse bulkUploadGarlands(Long categoryId, MultipartFile file) {
        log.info("[bulkUploadGarlands] categoryId={}, filename={}", categoryId, file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new FunctionalException(HttpStatus.BAD_REQUEST, "Uploaded CSV file is empty");
        }
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            throw new FunctionalException(HttpStatus.BAD_REQUEST, "File must be a .csv file");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("[bulkUploadGarlands] categoryId={} not found", categoryId);
                    return new FunctionalException(HttpStatus.NOT_FOUND, "Category not found: " + categoryId);
                });

        List<GarlandCsvRow> rows = parseCsv(file);

        List<GarlandBulkUploadItemResult> results = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (GarlandCsvRow row : rows) {
            try {
                GarlandBulkUploadItemResult result = garlandRowProcessor.processRow(row, category);
                results.add(result);
                successCount++;
            } catch (GarlandRowProcessingException e) {
                results.add(GarlandBulkUploadItemResult.builder()
                        .rowNumber(e.getRowNumber())
                        .productCode(e.getProductCode())
                        .status(GarlandRowStatus.FAILED)
                        .message(e.getMessage())
                        .build());
                failureCount++;
            }
        }

        log.info("[bulkUploadGarlands] categoryId={} total={}, success={}, failed={}",
                categoryId, rows.size(), successCount, failureCount);

        return GarlandBulkUploadResponse.builder()
                .totalRows(rows.size())
                .successCount(successCount)
                .failureCount(failureCount)
                .results(results)
                .build();
    }

    private List<GarlandCsvRow> parseCsv(MultipartFile file) {
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .setIgnoreSurroundingSpaces(true)
                .build();

        try (var reader = stripBomAndOpenReader(file);
             CSVParser parser = format.parse(reader)) {

            Set<String> headers = parser.getHeaderNames().stream()
                    .map(h -> h.replace("\uFEFF", "").trim().toLowerCase())
                    .collect(java.util.stream.Collectors.toSet());

            List<String> missing = REQUIRED_HEADERS.stream()
                    .filter(h -> !headers.contains(h))
                    .toList();
            if (!missing.isEmpty()) {
                throw new FunctionalException(HttpStatus.BAD_REQUEST,
                        "CSV is missing required column(s): " + String.join(", ", missing));
            }

            List<GarlandCsvRow> rows = new ArrayList<>();
            int rowNumber = 1; // header is row 1
            for (CSVRecord record : parser) {
                rowNumber++;
                rows.add(GarlandCsvRow.builder()
                        .rowNumber(rowNumber)
                        .name(safeGet(record, "name"))
                        .description(safeGet(record, "description"))
                        .orderBy(safeGet(record, "order_by"))
                        .price(safeGet(record, "price"))
                        .productCode(safeGet(record, "product_code"))
                        .imageUrl(safeGet(record, "image_url"))
                        .imageExtension(safeGet(record, "image_extension"))
                        .material(safeGet(record, "material"))
                        .build());
            }

            if (rows.isEmpty()) {
                throw new FunctionalException(HttpStatus.BAD_REQUEST, "CSV has no data rows");
            }
            return rows;

        } catch (IOException e) {
            log.error("[parseCsv] failed to read CSV file: {}", e.getMessage(), e);
            throw new FunctionalException(HttpStatus.BAD_REQUEST, "Could not read CSV file: " + e.getMessage());
        }
    }

    private Reader stripBomAndOpenReader(MultipartFile file) throws IOException {
        InputStream rawInputStream = file.getInputStream();
        PushbackInputStream pushbackInputStream = new PushbackInputStream(rawInputStream, 3);
        byte[] possibleBom = new byte[3];
        int bytesRead = pushbackInputStream.read(possibleBom, 0, 3);

        boolean isUtf8Bom = bytesRead == 3
                && (possibleBom[0] & 0xFF) == 0xEF
                && (possibleBom[1] & 0xFF) == 0xBB
                && (possibleBom[2] & 0xFF) == 0xBF;

        if (!isUtf8Bom && bytesRead > 0) {
            pushbackInputStream.unread(possibleBom, 0, bytesRead);
        }
        // If isUtf8Bom is true, the 3 BOM bytes are simply not pushed back — effectively stripped.

        return new InputStreamReader(pushbackInputStream, StandardCharsets.UTF_8);
    }

    private String safeGet(CSVRecord record, String column) {
        return (record.isMapped(column) && StringUtils.hasText(record.get(column)))
                ? record.get(column).trim()
                : null;
    }
}
