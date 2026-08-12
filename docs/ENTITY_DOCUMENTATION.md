# JPA Entity Classes - Documentation

## Overview
All 7 database tables have been converted to JPA entity classes with proper annotations, relationships, and lombok support.

## Entity Classes Created

### 1. Image Entity
**File:** `src/main/java/com/garland/entity/Image.java`

```java
@Entity
@Table(name = "image")
public class Image {
    - imageId: Integer (PK, Auto-generated)
    - imagePath: String (unique, NOT NULL)
    - size: Long
    - extension: String
    - createdAt: LocalDateTime (auto-timestamped)
    - updatedAt: LocalDateTime (auto-timestamped)
}
```

**Features:**
- Auto-generation of timestamps using Hibernate annotations
- Lombok support for getters, setters, and builders
- Used as a referenced entity by Category, Garland, and Company

---

### 2. Role Entity
**File:** `src/main/java/com/garland/entity/Role.java`

```java
@Entity
@Table(name = "role")
public class Role {
    - id: Integer (PK, Auto-generated)
    - roleName: String (unique, NOT NULL, 50 chars)
    - createdAt: LocalDateTime (auto-timestamped)
    - updatedAt: LocalDateTime (auto-timestamped)
}
```

**Features:**
- Unique constraint on roleName
- No relationships in this entity (relationships via UserRole)
- Default roles created by Liquibase: ADMIN, MANAGER, VIEWER

---

### 3. User Entity
**File:** `src/main/java/com/garland/entity/User.java`

```java
@Entity
@Table(name = "user")
public class User {
    - id: Integer (PK, Auto-generated)
    - username: String (unique, NOT NULL, 100 chars)
    - email: String (unique, NOT NULL, 255 chars)
    - password: String (NOT NULL, 255 chars - BCrypt hashed)
    - isActive: Boolean (default: true)
    - createdAt: LocalDateTime (auto-timestamped)
    - updatedAt: LocalDateTime (auto-timestamped)
    - userRoles: Set<UserRole> (one-to-many bidirectional)
}
```

**Relationships:**
- OneToMany → UserRole (cascade delete, orphan removal enabled)
- Fetch type: EAGER for roles

**Helper Methods:**
- `addRole(UserRole)` - Add role to user
- `removeRole(UserRole)` - Remove role from user

---

### 4. UserRole Entity
**File:** `src/main/java/com/garland/entity/UserRole.java`

```java
@Entity
@Table(name = "user_role", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
public class UserRole {
    - id: Integer (PK, Auto-generated)
    - user: User (ManyToOne, NOT NULL)
    - role: Role (ManyToOne, NOT NULL, EAGER fetch)
    - createdAt: LocalDateTime (auto-timestamped)
}
```

**Relationships:**
- ManyToOne → User (LAZY fetch)
- ManyToOne → Role (EAGER fetch)
- Unique constraint on (user_id, role_id) combination

**Purpose:**
- Junction table for User-Role many-to-many relationship
- No orphan removal to preserve role history

---

### 5. Category Entity
**File:** `src/main/java/com/garland/entity/Category.java`

```java
@Entity
@Table(name = "category")
public class Category {
    - id: Integer (PK, Auto-generated)
    - name: String (unique, NOT NULL, 100 chars)
    - order: Integer (default: 0)
    - description: String (TEXT, optional)
    - image: Image (ManyToOne, optional)
    - orderBy: String (default: "name", 50 chars)
    - isActive: Boolean (default: true, soft delete)
    - createdAt: LocalDateTime (auto-timestamped)
    - updatedAt: LocalDateTime (auto-timestamped)
    - garlands: Set<Garland> (one-to-many bidirectional)
}
```

**Relationships:**
- ManyToOne → Image (LAZY, optional)
- OneToMany → Garland (cascade delete, orphan removal)

**Helper Methods:**
- `addGarland(Garland)` - Add garland to category
- `removeGarland(Garland)` - Remove garland from category

**Soft Delete:**
- Use `isActive` flag for soft deletes (repository level filtering needed)

---

### 6. Garland Entity
**File:** `src/main/java/com/garland/entity/Garland.java`

```java
@Entity
@Table(name = "garland")
public class Garland {
    - id: Integer (PK, Auto-generated)
    - name: String (NOT NULL, 150 chars)
    - productCode: String (unique, NOT NULL, 50 chars)
    - price: BigDecimal (NOT NULL, 10,2 precision)
    - description: String (TEXT, optional)
    - orderBy: Integer (default: 0)
    - category: Category (ManyToOne, NOT NULL)
    - image: Image (ManyToOne, optional)
    - isAvailable: Boolean (default: true)
    - isActive: Boolean (default: true, soft delete)
    - createdAt: LocalDateTime (auto-timestamped)
    - updatedAt: LocalDateTime (auto-timestamped)
}
```

**Relationships:**
- ManyToOne → Category (LAZY, cascade delete)
- ManyToOne → Image (LAZY, optional)

**Validation:**
- Price field uses BigDecimal for financial calculations
- Database CHECK constraint ensures price > 0 (validate in service too)

**Flags:**
- isAvailable: Product availability
- isActive: Soft delete flag

---

### 7. Company Entity
**File:** `src/main/java/com/garland/entity/Company.java`

```java
@Entity
@Table(name = "company")
public class Company {
    - id: Integer (PK, Auto-generated)
    - name: String (NOT NULL, 150 chars)
    - description: String (TEXT, optional)
    - shortDescription: String (optional, 255 chars)
    - image: Image (ManyToOne, optional)
    - address: String (optional, 500 chars)
    - phoneNumber: String (optional, 20 chars)
    - emailId: String (optional, 255 chars)
    - whatsapp: String (optional, 20 chars)
    - isActive: Boolean (default: true, soft delete)
    - createdAt: LocalDateTime (auto-timestamped)
    - updatedAt: LocalDateTime (auto-timestamped)
}
```

**Relationships:**
- ManyToOne → Image (LAZY, optional)

**Special Constraint:**
- Single record enforced at application/repository level
- Database has id as PK, only one record with id=1

---

## DTO Classes

### Request DTOs
Located in `src/main/java/com/garland/dto/request/`

1. **LoginRequest**
   - username (required, 3-100 chars)
   - password (required, 6-255 chars)

2. **ForgetPasswordRequest**
   - email (required, valid email format)

3. **CategoryRequest**
   - name (required, 2-100 chars)
   - order (optional, >= 0)
   - description (optional, <= 1000 chars)
   - imageId (optional, positive)
   - orderBy (optional, <= 50 chars)

4. **GarlandRequest**
   - name (required, 2-150 chars)
   - productCode (required, unique, 5-50 chars)
   - price (required, 0.01-999999.99)
   - description (optional, <= 1000 chars)
   - orderBy (optional, >= 0)
   - categoryId (required, positive)
   - imageId (optional, positive)
   - isAvailable (optional)

5. **CompanyRequest**
   - name (optional, 2-150 chars)
   - description (optional, <= 2000 chars)
   - shortDescription (optional, <= 255 chars)
   - address (optional, <= 500 chars)
   - phoneNumber (optional, valid phone format)
   - emailId (optional, valid email)
   - whatsapp (optional, valid phone format)
   - imageId (optional)

### Response DTOs
Located in `src/main/java/com/garland/dto/response/`

1. **ImageResponse** - image metadata
2. **RoleResponse** - role info
3. **UserResponse** - user info with roles
4. **LoginResponse** - login success with token
5. **CategoryResponse** - category details
6. **GarlandResponse** - garland details
7. **CompanyResponse** - company details
8. **ApiResponse<T>** - Generic API response wrapper with helpers
9. **PaginatedResponse<T>** - Paginated list wrapper

---

## Mapper Classes

Located in `src/main/java/com/garland/mapper/`

1. **CategoryMapper** - Convert between Category entity and CategoryResponse
2. **GarlandMapper** - Convert between Garland entity and GarlandResponse
3. **CompanyMapper** - Convert between Company entity and CompanyResponse
4. **UserMapper** - Convert between User entity and UserResponse

**Features:**
- Null-safe conversion
- Relationship handling (extracting IDs)
- Spring component for dependency injection

---

## Key Design Decisions

### Lombok Usage
- `@Data` - Generates getters, setters, equals, hashCode, toString
- `@Builder` - Fluent builder pattern for object creation
- `@NoArgsConstructor` - JPA requirement for entity construction
- `@AllArgsConstructor` - Convenient for testing

### Timestamp Management
- `@CreationTimestamp` - Automatically set on creation
- `@UpdateTimestamp` - Automatically updated on modification
- Uses Hibernate annotations (org.hibernate.annotations)
- All timestamps in LocalDateTime for timezone safety

### Relationships
- Lazy loading by default for performance
- Eager loading for Role in UserRole (needed for authentication)
- Cascade delete for Category→Garland (delete category = delete garlands)
- Orphan removal for Category and User relationships

### Validation
- Bean Validation (Jakarta.validation) annotations
- Size constraints on string fields
- Positive/decimal min/max for numeric fields
- Email format validation
- Pattern validation for phone numbers

### Soft Deletes
- `isActive` Boolean flag on tables supporting soft deletes
- Set to false instead of deleting records
- Repository queries should filter `WHERE isActive = true`

### Special Cases
1. **User-Role Relationship:**
   - Many-to-many via UserRole junction table
   - Role loaded eagerly in UserRole (for authentication)
   - User roles loaded eagerly in User entity

2. **Company:**
   - Single record enforced in application
   - Initialize with id=1 by Liquibase migration
   - All operations on company use fixed id=1

3. **Price:**
   - Uses BigDecimal for financial accuracy
   - Database check constraint ensures > 0
   - Service layer should validate before save

---

## Usage Example

```java
// Creating a garland
Category category = categoryRepository.findById(1).orElseThrow();
Image image = imageRepository.findById(101).orElseThrow();

Garland garland = Garland.builder()
    .name("Rose Wedding Garland")
    .productCode("GAR-001")
    .price(new BigDecimal("1500.00"))
    .description("Beautiful red rose wedding garland")
    .category(category)
    .image(image)
    .isAvailable(true)
    .orderBy(1)
    .build();

garlandRepository.save(garland);

// Converting to response
GarlandResponse response = garlandMapper.toResponse(garland);
```

---

## Jakarta Persistence vs javax

**Note:** Using Jakarta Persistence (jakarta.persistence.*) not javax.persistence.*
- Jakarta is the new standard for Java EE 9+
- Supported by Spring Boot 3.x
- Required for compatibility with latest Spring versions

---

## Next Steps for Integration

1. Create repositories (JpaRepository extensions)
2. Create services with business logic
3. Create controllers with REST endpoints
4. Implement validation in service layer
5. Add repository specifications for filtering
6. Create custom repository methods as needed
7. Add entity lifecycle callbacks if needed
8. Configure cascade and lazy loading appropriately

