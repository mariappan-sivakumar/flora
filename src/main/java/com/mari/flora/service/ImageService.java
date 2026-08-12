package com.mari.flora.service;

import com.mari.flora.dto.response.ImageResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ImageResponseDto upload(MultipartFile file);
    ImageResponseDto getById(Long imageId);
    void delete(Long imageId);
}