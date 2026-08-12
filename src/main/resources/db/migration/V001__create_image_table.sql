-- Liquibase Migration: Create Image Table
-- Author: Admin System
-- Date: 2026-07-03
-- Description: Create image table for storing image metadata

--liquibase formatted sql

--changeset admin:001-create-image-table
CREATE TABLE image (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    image_path VARCHAR(500) NOT NULL,
    size BIGINT,
    extension VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_image_path (image_path)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--rollback DROP TABLE image;
