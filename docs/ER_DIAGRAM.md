# Entity Relationship Diagram - Garland Admin Portal

## Database Schema

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           DATABASE SCHEMA                                   │
└─────────────────────────────────────────────────────────────────────────────┘

                                  ┌──────────┐
                                  │  IMAGE   │
                                  ├──────────┤
                                  │ image_id │◄──────────────┐
                                  │ image_path
                                  │ size     │              │
                                  │ extension│              │
                                  └──────────┘              │
                                       ▲                    │
                                       │                    │
                    ┌──────────────────┼────────────────────┼───────────┐
                    │                  │                    │           │
              (1)   │            (1)   │              (1)   │     (1)   │
         ┌──────────┴────┐  ┌──────────┴────┐  ┌──────────┴────┐  ┌──────────┴────┐
         │   CATEGORY    │  │   GARLAND     │  │   COMPANY    │  │    USER      │
         ├───────────────┤  ├───────────────┤  ├──────────────┤  ├──────────────┤
         │ id (PK)       │  │ id (PK)       │  │ id (PK)      │  │ id (PK)      │
         │ name          │  │ name          │  │ name         │  │ username     │
         │ order         │  │ product_code  │  │ description  │  │ email        │
         │ description   │  │ price         │  │ short_desc   │  │ password     │
         │ image_id (FK) │  │ description   │  │ address      │  │ is_active    │
         │ order_by      │  │ order_by      │  │ phone_number │  │ created_at   │
         │ is_active     │  │ is_available  │  │ email_id     │  │ updated_at   │
         │ created_at    │  │ is_active     │  │ whatsapp     │  └──────────────┘
         │ updated_at    │  │ category_id (FK)  │ image_id (FK)│       ▲
         └──────────────┬┘  │ image_id (FK)│  │ is_active    │       │
                        │   │ created_at   │  │ created_at   │       │ (N)
                   (N)  │   │ updated_at   │  │ updated_at   │       │
                        │   └───────────────┘  └──────────────┘       │
                        │          ▲                                   │
                        └──────────┼───────────────────────────────────┤
                                   │                                   │
                              (M)  │  garland_category (FK)    (M)    │
                                   │                                   │
                    ┌──────────────┴────────────────┬──────────────────┘
                    │                               │
              ┌─────┴──────┐                  ┌─────┴──────┐
              │ USER_ROLE  │                  │    ROLE    │
              ├────────────┤                  ├────────────┤
              │ id (PK)    │                  │ id (PK)    │
              │ user_id (FK)────────┬─────────│ role_name  │
              │ role_id (FK)────────┴─────────│ created_at │
              │ created_at │                  │ updated_at │
              └────────────┘                  └────────────┘
              
              (M) user_id              (1) id
              (M) role_id              (1) id
```

## Table Definitions

### USER
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INT | PK, AUTO_INCREMENT | Unique user identifier |
| username | VARCHAR(100) | UNIQUE, NOT NULL | Login username |
| email | VARCHAR(255) | UNIQUE, NOT NULL | User email |
| password | VARCHAR(255) | NOT NULL | Hashed password (BCrypt) |
| is_active | BOOLEAN | DEFAULT TRUE | Soft delete flag |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update time |

### ROLE
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INT | PK, AUTO_INCREMENT | Unique role identifier |
| role_name | VARCHAR(50) | UNIQUE, NOT NULL | Role name (ADMIN, MANAGER, etc.) |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update time |

### USER_ROLE
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INT | PK, AUTO_INCREMENT | Unique mapping identifier |
| user_id | INT | FK → USER.id | Reference to user |
| role_id | INT | FK → ROLE.id | Reference to role |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| **Indexes** | | UNIQUE(user_id, role_id) | Prevent duplicate assignments |

### CATEGORY
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INT | PK, AUTO_INCREMENT | Unique category identifier |
| name | VARCHAR(100) | NOT NULL | Category name |
| order | INT | DEFAULT 0 | Display order |
| description | TEXT | | Category description |
| image_id | INT | FK → IMAGE.image_id | Category image |
| order_by | VARCHAR(50) | DEFAULT 'name' | Sort field |
| is_active | BOOLEAN | DEFAULT TRUE | Soft delete flag |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update time |

### GARLAND
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INT | PK, AUTO_INCREMENT | Unique garland identifier |
| name | VARCHAR(150) | NOT NULL | Product name |
| product_code | VARCHAR(50) | UNIQUE | Stock keeping unit |
| price | DECIMAL(10,2) | NOT NULL | Product price |
| description | TEXT | | Product description |
| order_by | INT | DEFAULT 0 | Display order |
| category_id | INT | FK → CATEGORY.id | Category reference |
| image_id | INT | FK → IMAGE.image_id | Product image |
| is_available | BOOLEAN | DEFAULT TRUE | Availability status |
| is_active | BOOLEAN | DEFAULT TRUE | Soft delete flag |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update time |

### COMPANY
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | INT | PK, AUTO_INCREMENT | Single record (enforced) |
| name | VARCHAR(150) | NOT NULL | Company name |
| description | TEXT | | Detailed description |
| short_description | VARCHAR(255) | | Brief description |
| image_id | INT | FK → IMAGE.image_id | Company logo/image |
| address | VARCHAR(500) | | Physical address |
| phone_number | VARCHAR(20) | | Contact phone |
| email_id | VARCHAR(255) | | Contact email |
| whatsapp | VARCHAR(20) | | WhatsApp number |
| is_active | BOOLEAN | DEFAULT TRUE | Soft delete flag |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update time |

### IMAGE
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| image_id | INT | PK, AUTO_INCREMENT | Unique image identifier |
| image_path | VARCHAR(500) | NOT NULL | Storage path (S3/local) |
| size | BIGINT | | File size in bytes |
| extension | VARCHAR(10) | | File extension (jpg, png, etc.) |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Upload time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update time |

## Relationships

| From | To | Type | Cardinality | Description |
|------|----|----|---|-------------|
| CATEGORY | IMAGE | 1:0..1 | (1) → (0..1) | Optional category image |
| GARLAND | CATEGORY | M:1 | (M) → (1) | Multiple garlands per category |
| GARLAND | IMAGE | 1:0..1 | (1) → (0..1) | Optional product image |
| COMPANY | IMAGE | 1:0..1 | (1) → (0..1) | Optional company logo |
| USER | ROLE | M:M | (M) → (M) | Via USER_ROLE join table |

## Constraints & Indexes

- **Unique Constraints**: username, email (USER), role_name (ROLE), product_code (GARLAND), user_id + role_id (USER_ROLE)
- **Foreign Keys**: CASCADE DELETE on image references, CASCADE DELETE on category in garland
- **Indexes**: user_id, role_id on USER_ROLE; category_id on GARLAND; is_active on all tables for soft deletes
