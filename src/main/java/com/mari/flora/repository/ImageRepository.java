package com.mari.flora.repository;

import com.mari.flora.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
