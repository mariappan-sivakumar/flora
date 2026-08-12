-- Liquibase Migration: Create Garland Table
-- Author: Admin System
-- Date: 2026-07-03
-- Description: Create garland table for storing product information

--liquibase formatted sql

--changeset admin:007-create-garland-table
CREATE TABLE garland (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    order_by INT DEFAULT 0,
    category_id INT NOT NULL,
    image_id INT,
    is_available BOOLEAN DEFAULT TRUE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_garland_category_id FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    CONSTRAINT fk_garland_image_id FOREIGN KEY (image_id) REFERENCES image(image_id) ON DELETE SET NULL,
    CONSTRAINT chk_price_positive CHECK (price > 0),
    INDEX idx_category_id (category_id),
    INDEX idx_product_code (product_code),
    INDEX idx_is_active (is_active),
    INDEX idx_is_available (is_available),
    UNIQUE KEY uk_garland_product_code (product_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--rollback DROP TABLE garland;
