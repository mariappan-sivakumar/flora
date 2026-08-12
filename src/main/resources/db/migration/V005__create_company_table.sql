-- Liquibase Migration: Create Company Table
-- Author: Admin System
-- Date: 2026-07-03
-- Description: Create company table for storing company configuration

--liquibase formatted sql

--changeset admin:008-create-company-table
CREATE TABLE company (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    short_description VARCHAR(255),
    image_id INT,
    address VARCHAR(500),
    phone_number VARCHAR(20),
    email_id VARCHAR(255),
    whatsapp VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_company_image_id FOREIGN KEY (image_id) REFERENCES image(image_id) ON DELETE SET NULL,
    UNIQUE KEY uk_company_single_record (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--rollback DROP TABLE company;

--changeset admin:009-insert-default-company
INSERT INTO company (id, name, is_active) VALUES (1, 'Default Company', TRUE);

--rollback DELETE FROM company WHERE id = 1;
