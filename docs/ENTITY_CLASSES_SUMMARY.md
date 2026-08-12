# JPA Entity Classes Summary - Garland Admin Portal

## 📋 Quick Reference

**Date Created:** 2026-07-03  
**Phase:** Phase 2 - Entity & DTO Creation  
**Status:** ✅ COMPLETE  
**Total Files:** 25  

---

## 📦 What Was Created

### 1. Entity Classes (7)
All database tables converted to JPA entities with proper annotations and relationships.

```
Image.java          → Stores image metadata
Role.java           → Role definitions
User.java           → Admin users with bidirectional role mapping
UserRole.java       → Junction table for User-Role M:M relationship
Category.java       → Product categories
Garland.java        → Garland products
Company.java        → Company configuration
```

### 2. Request DTOs (5)
Input validation classes for API requests with Jakarta Validation annotations.

```
LoginRequest.java           → username, password
ForgetPasswordRequest.java  → email
CategoryRequest.java        → category creation/update data
GarlandRequest.java         → garland creation/update data
CompanyRequest.java         → company creation/update data
```

### 3. Response DTOs (9)
Output classes for API responses with clean JSON serialization.

```
ImageResponse.java      → Image data
RoleResponse.java       → Role data
UserResponse.java       → User data with roles
LoginResponse.java      → JWT token + user info
CategoryResponse.java   → Category data
GarlandResponse.java    → Garland data
CompanyResponse.java    → Company data
ApiResponse<T>          → Generic wrapper with helper methods
PaginatedResponse<T>    → Paginated list wrapper
```

### 4. Mapper Classes (4)
Convert between entities and DTOs with null-safe operations.

```
CategoryMapper.java → Category ↔ CategoryResponse
GarlandMapper.java  → Garland ↔ GarlandResponse
CompanyMapper.java  → Company ↔ CompanyResponse
UserMapper.java     → User → UserResponse
```

---

## 🔗 Relationship Map

### User-Role (Many-to-Many)
```
User (1) ─────────────┐
                      ├─→ UserRole ─→ Role (M)
                      │   (junction)
User (M) ◄────────────┘
```
- **User Entity:** EAGER fetch of userRoles (needed for authentication)
- **UserRole Entity:** LAZY fetch of user, EAGER fetch of role
- **Cascade:** DELETE on user removes all user roles
- **Orphan Removal:** Enabled on User.userRoles

### Category-Garland (One-to-Many)
```
Category (1) ─────────────────→ Garland (M)
```
- **Category Entity:** Lazy fetch of garlands
- **Garland Entity:** Many-to-one with NOT NULL foreign key
- **Cascade:** DELETE on category removes all garlands
- **Orphan Removal:** Enabled on Category.garlands

### Image References (One-to-Optional)
```
Image ─────→ Category (optional)
Image ─────→ Garland (optional)
Image ─────→ Company (optional)
```
- All lazy loaded
- FK SET NULL on image delete

---

## 🏗️ Entity Details

### Image Entity
**Purpose:** Store image metadata and path references

| Field | Type | JPA Mapping | Constraints |
|-------|------|---------|-------------|
| imageId | Integer | @Id @GeneratedValue | PK, auto-increment |
| imagePath | String | @Column | 500 chars, unique, NOT NULL |
| size | Long | @Column | Optional |
| extension | String | @Column | 10 chars |
| createdAt | LocalDateTime | @CreationTimestamp | Auto-set |
| updatedAt | LocalDateTime | @UpdateTimestamp | Auto-update |

---

### Role Entity
**Purpose:** Define available roles for users

| Field | Type | JPA Mapping | Constraints |
|-------|------|---------|-------------|
| id | Integer | @Id @GeneratedValue | PK |
| roleName | String | @Column | 50 chars, unique, NOT NULL |
| createdAt | LocalDateTime | @CreationTimestamp | Auto-set |
| updatedAt | LocalDateTime | @UpdateTimestamp | Auto-update |

**Predefined Roles (via Liquibase):**
- ADMIN - Full system access
- MANAGER - Manage categories and garlands
- VIEWER - Read-only access

---

### User Entity
**Purpose:** Store admin user credentials and role assignments

| Field | Type | JPA Mapping | Constraints |
|-------|------|---------|-------------|
| id | Integer | @Id @GeneratedValue | PK |
| username | String | @Column | 100 chars, unique, NOT NULL |
| email | String | @Column | 255 chars, unique, NOT NULL |
| password | String | @Column | 255 chars, NOT NULL (BCrypt) |
| isActive | Boolean | @Column | default: true |
| createdAt | LocalDateTime | @CreationTimestamp | Auto-set |
| updatedAt | LocalDateTime | @UpdateTimestamp | Auto-update |
| userRoles | Set<UserRole> | @OneToMany | EAGER, cascade delete |

**Methods:**
- `addRole(UserRole)` - Add role with bidirectional update
- `removeRole(UserRole)` - Remove role with cleanup

**Authentication:**
- Password must be BCrypt hashed before saving
- Use isActive flag for soft delete

---

### UserRole Entity
**Purpose:** Map users to roles (junction table for M:M)

| Field | Type | JPA Mapping | Constraints |
|-------|------|---------|-------------|
| id | Integer | @Id @GeneratedValue | PK |
| user | User | @ManyToOne(LAZY) | NOT NULL, FK with cascade |
| role | Role | @ManyToOne(EAGER) | NOT NULL |
| createdAt | LocalDateTime | @CreationTimestamp | Auto-set |

**Unique Constraint:** (user_id, role_id) - prevent duplicate assignments

---

### Category Entity
**Purpose:** Organize garlands into categories

| Field | Type | JPA Mapping | Constraints |
|-------|------|---------|-------------|
| id | Integer | @Id @GeneratedValue | PK |
| name | String | @Column | 100 chars, unique, NOT NULL |
| order | Integer | @Column | default: 0 |
| description | String | @Column(TEXT) | Optional |
| image | Image | @ManyToOne(LAZY) | Optional |
| orderBy | String | @Column | 50 chars, default: "name" |
| isActive | Boolean | @Column | default: true (soft delete) |
| createdAt | LocalDateTime | @CreationTimestamp | Auto-set |
| updatedAt | LocalDateTime | @UpdateTimestamp | Auto-update |
| garlands | Set<Garland> | @OneToMany | LAZY, cascade delete |

**Methods:**
- `addGarland(Garland)` - Add garland with bidirectional update
- `removeGarland(Garland)` - Remove garland with cleanup

---

### Garland Entity
**Purpose:** Represent actual garland products

| Field | Type | JPA Mapping | Constraints |
|-------|------|---------|-------------|
| id | Integer | @Id @GeneratedValue | PK |
| name | String | @Column | 150 chars, NOT NULL |
| productCode | String | @Column | 50 chars, unique, NOT NULL |
| price | BigDecimal | @Column | 10,2 precision, NOT NULL |
| description | String | @Column(TEXT) | Optional |
| orderBy | Integer | @Column | default: 0 |
| category | Category | @ManyToOne(LAZY) | NOT NULL, cascade delete |
| image | Image | @ManyToOne(LAZY) | Optional |
| isAvailable | Boolean | @Column | default: true |
| isActive | Boolean | @Column | default: true (soft delete) |
| createdAt | LocalDateTime | @CreationTimestamp | Auto-set |
| updatedAt | LocalDateTime | @UpdateTimestamp | Auto-update |

**Important:**
- Uses BigDecimal for price (financial precision)
- productCode must be unique (business key)
- Category is mandatory (every garland must have category)

---

### Company Entity
**Purpose:** Store company configuration and details

| Field | Type | JPA Mapping | Constraints |
|-------|------|---------|-------------|
| id | Integer | @Id @GeneratedValue | PK |
| name | String | @Column | 150 chars, NOT NULL |
| description | String | @Column(TEXT) | Optional |
| shortDescription | String | @Column | 255 chars |
| image | Image | @ManyToOne(LAZY) | Optional |
| address | String | @Column | 500 chars |
| phoneNumber | String | @Column | 20 chars (validate in DTO) |
| emailId | String | @Column | 255 chars (validate in DTO) |
| whatsapp | String | @Column | 20 chars (validate in DTO) |
| isActive | Boolean | @Column | default: true |
| createdAt | LocalDateTime | @CreationTimestamp | Auto-set |
| updatedAt | LocalDateTime | @UpdateTimestamp | Auto-update |

**Special Constraint:**
- Only one record should exist (id = 1)
- Repository should enforce this at application level

---

## 📋 Validation Rules

### Request DTO Validations

#### LoginRequest
```
- username: NotBlank, Size(3-100)
- password: NotBlank, Size(6-255)
```

#### ForgetPasswordRequest
```
- email: NotBlank, Email
```

#### CategoryRequest
```
- name: NotBlank, Size(2-100)
- order: Min(0)
- description: Size(max=1000)
- imageId: Positive
- orderBy: Size(max=50)
```

#### GarlandRequest
```
- name: NotBlank, Size(2-150)
- productCode: NotBlank, Size(5-50)
- price: NotNull, DecimalMin(0.01), DecimalMax(999999.99)
- description: Size(max=1000)
- orderBy: Min(0)
- categoryId: NotNull, Positive
- imageId: Positive
- isAvailable: Optional
```

#### CompanyRequest
```
- name: Size(2-150)
- description: Size(max=2000)
- shortDescription: Size(max=255)
- address: Size(max=500)
- phoneNumber: Pattern(+?[0-9]{1,15})
- emailId: Email
- whatsapp: Pattern(+?[0-9]{1,15})
- imageId: Positive
```

---

## 🔄 Data Flow Example

### Create Garland Flow

```
1. Client sends → POST /api/garlands with GarlandRequest

2. Controller receives:
   @Valid @RequestBody GarlandRequest

3. Validation happens automatically (Bean Validation)

4. Controller calls → service.createGarland(request)

5. Service:
   • Fetches category from repository
   • Validates business rules (price > 0, etc.)
   • Creates Garland entity
   • Calls repository.save()

6. Repository saves to database via JPA

7. Service maps → GarlandResponse using GarlandMapper

8. Controller returns → ApiResponse.success(garlandResponse)

9. Response sent to client as JSON
```

---

## 📊 JSON Response Examples

### Success Response
```json
{
  "statusCode": 200,
  "message": "Operation successful",
  "data": {
    "id": 1,
    "name": "Rose Wedding Garland",
    "productCode": "GAR-001",
    "price": 1500.00
  },
  "timestamp": 1688404451000
}
```

### Error Response
```json
{
  "statusCode": 400,
  "error": "Validation failed",
  "message": "Invalid input",
  "timestamp": 1688404451000
}
```

### Paginated Response
```json
{
  "content": [
    { "id": 1, "name": "Category 1" },
    { "id": 2, "name": "Category 2" }
  ],
  "currentPage": 0,
  "pageSize": 20,
  "totalElements": 45,
  "totalPages": 3,
  "hasNextPage": true,
  "hasPreviousPage": false
}
```

---

## 🛠️ Usage Guide

### Creating an Entity with Lombok

```java
// Using builder pattern
Garland garland = Garland.builder()
    .name("Rose Garland")
    .productCode("GAR-001")
    .price(new BigDecimal("1500.00"))
    .category(category)
    .isAvailable(true)
    .build();

// Lombok generates: toString(), equals(), hashCode(), getters, setters
System.out.println(garland);  // Uses @ToString
```

### Converting Entity to DTO

```java
// Using mapper
GarlandResponse response = garlandMapper.toResponse(garland);

// Response will be clean JSON (no null fields due to @JsonInclude)
```

### Adding Role to User

```java
User user = userRepository.findById(1).orElseThrow();
Role adminRole = roleRepository.findByRoleName("ADMIN").orElseThrow();

UserRole userRole = UserRole.builder()
    .role(adminRole)
    .build();

user.addRole(userRole);  // Bidirectional update
userRepository.save(user);
```

---

## 🔐 Security Notes

### Password Handling
```java
// Never store plaintext passwords
// Use BCrypt before saving:
String hashedPassword = bCryptPasswordEncoder.encode(password);
user.setPassword(hashedPassword);
```

### Soft Deletes
```java
// Don't actually delete, set isActive = false
user.setIsActive(false);
userRepository.save(user);

// Query should filter: WHERE is_active = true
```

### Role-Based Access
```java
// Get user roles from UserRole relationships
Set<String> roles = user.getRoles().stream()
    .map(Role::getRoleName)
    .collect(Collectors.toSet());
```

---

## 📦 Maven/Gradle Dependencies

The entities require these dependencies in `pom.xml` or `build.gradle`:

```xml
<!-- Spring Boot Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <scope>provided</scope>
</dependency>

<!-- Jakarta Validation -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
</dependency>

<!-- Hibernate Validator -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>

<!-- Database (choose one) -->
<!-- MySQL -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

---

## ✅ Checklist for Next Phase

- [ ] Create Repository interfaces (JpaRepository extensions)
- [ ] Implement custom repository methods
- [ ] Create Service classes with business logic
- [ ] Add transaction management (@Transactional)
- [ ] Create Controller classes with REST endpoints
- [ ] Add request validation error handling
- [ ] Setup Spring Security with JWT
- [ ] Add unit tests for entities
- [ ] Add integration tests for repositories
- [ ] Configure application.properties for JPA/Hibernate

---

## 📚 References

- [Jakarta Persistence Documentation](https://jakarta.ee/specifications/persistence/)
- [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)
- [Lombok Features](https://projectlombok.org/features/all)
- [Jakarta Validation Annotations](https://jakarta.ee/specifications/bean-validation/)
- [Hibernate Annotations](https://www.hibernate.org/orm/)

