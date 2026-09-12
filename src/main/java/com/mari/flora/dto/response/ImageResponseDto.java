package com.mari.flora.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Image upload or retrieval payload")
@Data
@Builder
public class ImageResponseDto {
    @Schema(description = "Image database ID", example = "5")
    private Integer imageId;
    @Schema(description = "Public image URL", example = "https://cdn.example.com/images/flower.jpg")
    private String imageUrl;
    @Schema(description = "Image extension", example = "jpg")
    private String extension;
    @Schema(description = "Image size in bytes", example = "245678")
    private Long sizeInBytes;
}