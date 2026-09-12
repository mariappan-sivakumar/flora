package com.mari.flora.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Image metadata returned to clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {

    @Schema(description = "Image database ID", example = "5")
    private Integer imageId;
    @Schema(description = "Public image path or URL", example = "https://cdn.example.com/images/flower.jpg")
    private String imagePath;
    @Schema(description = "Image size in bytes", example = "245678")
    private Long size;
    @Schema(description = "Image extension", example = "jpg")
    private String extension;
}
