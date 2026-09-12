package com.mari.flora.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mari.flora.config.CloudinaryProperties;
import com.mari.flora.config.ImageStorageProperties;
import com.mari.flora.dto.response.ImageResponseDto;
import com.mari.flora.entity.Image;
import com.mari.flora.exception.ResourceNotFoundException;
import com.mari.flora.repository.ImageRepository;
import com.mari.flora.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;
    private final CloudinaryProperties props;
    private final ImageStorageProperties storageProperties;

    @Override
    @Transactional
    public ImageResponseDto upload(MultipartFile file) {
        validateFile(file);
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder",          props.getFolder(),
                            "resource_type",   "image",
                            "use_filename",    true,
                            "unique_filename", true
                    )
            );

            Image saved = imageRepository.save(
                    Image.builder()
                            .imagePath((String) result.get("secure_url"))
                            .extension(extractExtension(file.getOriginalFilename()))
                            .size(file.getSize())
                            .build()
            );

            log.info("Image uploaded — imageId={} url={}", saved.getImageId(), saved.getImagePath());
            return toDto(saved);

        } catch (IOException e) {
            log.error("Cloudinary upload failed — file={}", file.getOriginalFilename(), e);
            throw new RuntimeException("Image upload failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ImageResponseDto getById(Long imageId) {
        log.info("getById called imageId={}", imageId);
        ImageResponseDto response = toDto(findOrThrow(imageId));
        log.debug("getById result={}", response);
        return response;
    }

    @Override
    @Transactional
    public void delete(Long imageId) {
        Image image = findOrThrow(imageId);
        try {
            cloudinary.uploader().destroy(
                    extractPublicId(image.getImagePath()),
                    ObjectUtils.emptyMap()
            );
            imageRepository.delete(image);
            log.info("Image deleted — imageId={}", imageId);
        } catch (IOException e) {
            log.error("Cloudinary delete failed — imageId={}", imageId, e);
            throw new RuntimeException("Image delete failed: " + e.getMessage(), e);
        }
    }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    private Image findOrThrow(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found: " + imageId));
    }

    private ImageResponseDto toDto(Image image) {
        return ImageResponseDto.builder()
                .imageId(image.getImageId())
                .imageUrl(image.getImagePath())
                .extension(image.getExtension())
                .sizeInBytes(image.getSize())
                .build();
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }
        String ext = extractExtension(file.getOriginalFilename());
        if (!storageProperties.getAllowedExtensions().contains(ext)) {
            throw new IllegalArgumentException("Unsupported file type: " + ext);
        }
        if (file.getSize() > storageProperties.getMaxSizeBytes()) { // 5 MB cap
            throw new IllegalArgumentException("File size exceeds limit");
        }
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    /**
     * https://res.cloudinary.com/<cloud>/image/upload/v123/imai-flora/abc.jpg
     *   → imai-flora/abc
     */
    private String extractPublicId(String url) {
        String marker = "/upload/";
        int idx = url.indexOf(marker);
        if (idx == -1) return url;
        String after = url.substring(idx + marker.length());
        if (after.startsWith("v") && after.contains("/")) {
            after = after.substring(after.indexOf('/') + 1);
        }
        int dot = after.lastIndexOf('.');
        return dot != -1 ? after.substring(0, dot) : after;
    }
}