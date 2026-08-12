package com.mari.flora.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponseDto {
    private Integer imageId;
    private String imageUrl;
    private String extension;
    private Long sizeInBytes;
}