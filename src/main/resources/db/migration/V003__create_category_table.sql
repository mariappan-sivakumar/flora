-- Liquibase Migration: Create Category Table
-- Author: Admin System
-- Date: 2026-07-03
-- Description: Create category table for grouping garlands

--liquibase formatted sql

--changeset admin:006-create-category-table
CREATE TABLE category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    `order` INT DEFAULT 0,
    description TEXT,
    image_id INT,
    order_by VARCHAR(50) DEFAULT 'name',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_category_image_id FOREIGN KEY (image_id) REFERENCES image(image_id) ON DELETE SET NULL,
    INDEX idx_is_active (is_active),
    INDEX idx_name (name),
    UNIQUE KEY uk_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--rollback DROP TABLE category;
