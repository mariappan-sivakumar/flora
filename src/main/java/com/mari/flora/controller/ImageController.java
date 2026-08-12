package com.mari.flora.controller;

import com.mari.flora.dto.response.ImageResponseDto;
import com.mari.flora.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "Image", description = "Cloudinary image upload, retrieval and deletion")
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Step 1 — Upload image, returns imageId to attach to garland/category")
    public ResponseEntity<ImageResponseDto> upload(@RequestParam("file") MultipartFile file) {
        log.info("upload called, fileName={}", file != null ? file.getOriginalFilename() : "null");
        ImageResponseDto response = imageService.upload(file);
        log.debug("upload result={}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{imageId}")
    @Operation(summary = "Get image URL by imageId — UI uses imageUrl as <img src>)")
    public ResponseEntity<ImageResponseDto> getById(@PathVariable Long imageId) {
        log.info("getById called, imageId={}", imageId);
        ImageResponseDto response = imageService.getById(imageId);
        log.debug("getById result={}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{imageId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SALES')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Delete image from Cloudinary and DB")
    public ResponseEntity<Void> delete(@PathVariable Long imageId) {
        log.info("delete called, imageId={}", imageId);
        imageService.delete(imageId);
        log.debug("delete completed for imageId={}", imageId);
        return ResponseEntity.noContent().build();
    }
}