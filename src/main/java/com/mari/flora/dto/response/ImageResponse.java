package com.mari.flora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {

    private Integer imageId;
    private String imagePath;
    private Long size;
    private String extension;
}
