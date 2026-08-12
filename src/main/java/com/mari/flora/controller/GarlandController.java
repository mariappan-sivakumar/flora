package com.mari.flora.controller;

import com.mari.flora.dto.request.GarlandRequest;
import com.mari.flora.dto.response.GarlandBulkUploadResponse;
import com.mari.flora.dto.response.GarlandResponse;
import com.mari.flora.dto.response.ResponseDto;
import com.mari.flora.service.GarlandService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/garland")
@Tag(name = "Garland", description = "APIs for managing garlands")
@Slf4j
public class GarlandController {
    private final GarlandService garlandService;

    public GarlandController(GarlandService garlandService) {
        this.garlandService = garlandService;
    }

    @Operation(summary = "Get all garlands", description = "Retrieve paginated list of garlands with optional filters")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "List returned"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping
    public ResponseEntity<ResponseDto<Page<GarlandResponse>>> getGarlands(@RequestParam(required = false) String search,
                                                                         @RequestParam(required = false) String productCode,
                                                                         @RequestParam(required = false) String category,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size,
                                                                         @RequestParam(defaultValue = "name") String sortBy,
                                                                         @RequestParam(defaultValue = "asc") String sortDirection) {
        log.info("getGarlands called search={}, productCode={}, category={}, page={}, size={}, sortBy={}, sortDirection={}", search, productCode, category, page, size, sortBy, sortDirection);
        Page<GarlandResponse> result = garlandService.getAllGarlands(search, productCode, category, page, size, sortBy, sortDirection);
        log.debug("getGarlands result={}", result);
        return ResponseEntity.ok(ResponseDto.success(result));

    }

    @Operation(summary = "Get garland by product code", description = "Retrieve a single garland by product code")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Garland returned"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/{productCode}")
    public ResponseEntity<ResponseDto<GarlandResponse>> getGarlandByProductCode(@PathVariable String productCode) {
        log.info("getGarlandByProductCode called productCode={}", productCode);
        GarlandResponse result = garlandService.getGarlandByProductCode(productCode);
        log.debug("getGarlandByProductCode result={}", result);
        return ResponseEntity.ok(ResponseDto.success(result));

    }

    @Operation(summary = "Create garland", description = "Create a new garland (admin only)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Garland created"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDto<String>> createGarland(@RequestBody GarlandRequest garlandRequest) {
        log.info("createGarland called request={}", garlandRequest);
        String result = garlandService.createGarland(garlandRequest);
        log.debug("createGarland result={}", result);
        return ResponseEntity.ok(ResponseDto.success(result));

    }

    @Operation(summary = "Update garland", description = "Update an existing garland (admin only)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Garland updated"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseDto<String>> updateGarland(@RequestBody GarlandRequest garlandRequest) {
        log.info("updateGarland called request={}", garlandRequest);
        try {
            String result = garlandService.updateGarland(garlandRequest);
            log.debug("updateGarland result={}", result);
            return ResponseEntity.ok(ResponseDto.success(result));
        } catch (Exception e) {
            log.error("Error while trying to update the garland", e);
            return ResponseEntity.status(500).body(ResponseDto.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error while trying to update the garland: " + e.getMessage()));
        }
    }

    @Operation(summary = "Delete garland", description = "Delete a garland by product code (admin only)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Garland deleted"), @ApiResponse(responseCode = "500", description = "Internal server error")})
    @DeleteMapping("/{productCode}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> deleteGarland(@PathVariable String productCode) {
        log.info("deleteGarland called productCode={}", productCode);
        try {
            String result = garlandService.deleteGarlandByProductCode(productCode);
            log.debug("deleteGarland result={}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error while trying to delete the garland", e);
            return ResponseEntity.status(500).body("Error while trying to delete the garland: " + e.getMessage());
        }
    }
    @PostMapping(value = "/{categoryId}/garlands/bulk-upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Bulk create or update garlands from a CSV file",
            description = "Rows are matched by product_code: existing codes are updated, new codes are created. " +
                    "Each row is processed independently — one invalid row does not fail the batch."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CSV processed; see per-row results in the response body"),
            @ApiResponse(responseCode = "400", description = "Invalid file, missing columns, or empty file"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<GarlandBulkUploadResponse> bulkUploadGarlands(
            @Parameter(description = "ID of the category the garlands belong to", example = "1")
            @PathVariable Long categoryId,
            @Parameter(description = "CSV file with columns: name, description, order_by, price, product_code, image_url, image_extension, material")
            @RequestParam("file") MultipartFile file) {

        log.info("[bulkUploadGarlands] categoryId={}, filename={}, size={} bytes",
                categoryId, file.getOriginalFilename(), file.getSize());

        GarlandBulkUploadResponse response = garlandService.bulkUploadGarlands(categoryId, file);

        log.info("[bulkUploadGarlands] categoryId={} total={}, success={}, failed={}",
                categoryId, response.getTotalRows(), response.getSuccessCount(), response.getFailureCount());

        return ResponseEntity.ok(response);
    }
}
