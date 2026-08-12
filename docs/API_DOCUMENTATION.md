# Garland Admin Portal - API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
All endpoints (except `/auth/login`) require JWT Bearer token in Authorization header:
```
Authorization: Bearer <JWT_TOKEN>
```

---

## 1. AUTHENTICATION ENDPOINTS

### 1.1 Login
**Endpoint:** `POST /auth/login`
**Access:** Public
**Description:** Admin user login with username and password to get JWT token

**Request Body:**
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@garland.com",
    "roles": ["ADMIN"]
  },
  "expiresIn": 3600
}
```

**Response (401 Unauthorized):**
```json
{
  "error": "Invalid username or password",
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**Curl Example:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'
```

---

### 1.2 Logout
**Endpoint:** `POST /auth/logout`
**Access:** Protected (Requires JWT)
**Description:** Invalidate current JWT token and logout

**Request Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Response (200 OK):**
```json
{
  "message": "Logout successful",
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**Curl Example:**
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 1.3 Forget Password
**Endpoint:** `POST /auth/forget-password`
**Access:** Public
**Description:** Request password reset link (email sent with reset token)

**Request Body:**
```json
{
  "email": "admin@garland.com"
}
```

**Response (200 OK):**
```json
{
  "message": "Password reset link sent to your email",
  "email": "admin@garland.com",
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**Response (404 Not Found):**
```json
{
  "error": "User with email not found",
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**Curl Example:**
```bash
curl -X POST http://localhost:8080/api/auth/forget-password \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@garland.com"}'
```

---

## 2. CATEGORY MANAGEMENT ENDPOINTS

### 2.1 Get All Categories
**Endpoint:** `GET /categories?page=0&size=20&sortBy=name&sortOrder=ASC`
**Access:** Protected (ADMIN, MANAGER, VIEWER)
**Description:** Retrieve list of all active categories with pagination

**Query Parameters:**
- `page` (int, optional): Page number (default: 0)
- `size` (int, optional): Page size (default: 20)
- `sortBy` (string, optional): Sort field - name, order, created_at (default: name)
- `sortOrder` (string, optional): ASC or DESC (default: ASC)
- `search` (string, optional): Search by category name

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Wedding Garlands",
      "order": 1,
      "description": "Traditional wedding garlands",
      "imageId": 101,
      "orderBy": "name",
      "isActive": true,
      "createdAt": "2026-07-03T15:00:00Z",
      "updatedAt": "2026-07-03T20:00:00Z"
    },
    {
      "id": 2,
      "name": "Festival Garlands",
      "order": 2,
      "description": "Festival celebration garlands",
      "imageId": 102,
      "orderBy": "name",
      "isActive": true,
      "createdAt": "2026-07-03T15:30:00Z",
      "updatedAt": "2026-07-03T20:15:00Z"
    }
  ],
  "totalElements": 5,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 20
}
```

**Curl Example:**
```bash
curl -X GET "http://localhost:8080/api/categories?page=0&size=20&sortBy=name" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 2.2 Get Single Category
**Endpoint:** `GET /categories/{id}`
**Access:** Protected (ADMIN, MANAGER, VIEWER)
**Description:** Get details of a specific category

**Path Parameters:**
- `id` (int): Category ID

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Wedding Garlands",
  "order": 1,
  "description": "Traditional wedding garlands and decorations",
  "imageId": 101,
  "orderBy": "name",
  "isActive": true,
  "createdAt": "2026-07-03T15:00:00Z",
  "updatedAt": "2026-07-03T20:00:00Z"
}
```

**Response (404 Not Found):**
```json
{
  "error": "Category with id 999 not found",
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**Curl Example:**
```bash
curl -X GET http://localhost:8080/api/categories/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 2.3 Create Category
**Endpoint:** `POST /categories`
**Access:** Protected (ADMIN only)
**Description:** Create a new category

**Request Body:**
```json
{
  "name": "Festival Garlands",
  "order": 2,
  "description": "Beautiful festival celebration garlands",
  "imageId": 102,
  "orderBy": "name"
}
```

**Response (201 Created):**
```json
{
  "id": 2,
  "name": "Festival Garlands",
  "order": 2,
  "description": "Beautiful festival celebration garlands",
  "imageId": 102,
  "orderBy": "name",
  "isActive": true,
  "createdAt": "2026-07-03T20:34:11Z",
  "updatedAt": "2026-07-03T20:34:11Z"
}
```

**Response (400 Bad Request):**
```json
{
  "error": "Category name already exists",
  "details": {
    "name": "Name must be unique"
  },
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**Curl Example:**
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Festival Garlands",
    "order": 2,
    "description": "Beautiful festival celebration garlands",
    "imageId": 102,
    "orderBy": "name"
  }'
```

---

### 2.4 Update Category
**Endpoint:** `PUT /categories/{id}`
**Access:** Protected (ADMIN only)
**Description:** Update an existing category

**Path Parameters:**
- `id` (int): Category ID

**Request Body:**
```json
{
  "name": "Festival Garlands Updated",
  "order": 3,
  "description": "Updated description",
  "imageId": 103,
  "orderBy": "order"
}
```

**Response (200 OK):**
```json
{
  "id": 2,
  "name": "Festival Garlands Updated",
  "order": 3,
  "description": "Updated description",
  "imageId": 103,
  "orderBy": "order",
  "isActive": true,
  "createdAt": "2026-07-03T20:34:11Z",
  "updatedAt": "2026-07-03T21:00:00Z"
}
```

**Curl Example:**
```bash
curl -X PUT http://localhost:8080/api/categories/2 \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Festival Garlands Updated",
    "order": 3,
    "description": "Updated description"
  }'
```

---

### 2.5 Delete Category
**Endpoint:** `DELETE /categories/{id}`
**Access:** Protected (ADMIN only)
**Description:** Soft delete a category (sets is_active to false)

**Path Parameters:**
- `id` (int): Category ID

**Response (204 No Content):**
No response body

**Response (404 Not Found):**
```json
{
  "error": "Category with id 999 not found",
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**Curl Example:**
```bash
curl -X DELETE http://localhost:8080/api/categories/2 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 3. GARLAND MANAGEMENT ENDPOINTS

### 3.1 Get All Garlands
**Endpoint:** `GET /garlands?page=0&size=20&sortBy=name&sortOrder=ASC`
**Access:** Protected (ADMIN, MANAGER, VIEWER)
**Description:** Retrieve all garlands with pagination

**Query Parameters:**
- `page` (int, optional): Page number (default: 0)
- `size` (int, optional): Page size (default: 20)
- `sortBy` (string, optional): Sort field - name, price, created_at (default: name)
- `sortOrder` (string, optional): ASC or DESC (default: ASC)
- `search` (string, optional): Search by garland name or product code

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Rose Wedding Garland",
      "productCode": "GARLAND-001",
      "price": 1500.00,
      "description": "Beautiful red rose wedding garland",
      "orderBy": 1,
      "categoryId": 1,
      "imageId": 201,
      "isAvailable": true,
      "isActive": true,
      "createdAt": "2026-07-03T15:00:00Z",
      "updatedAt": "2026-07-03T20:00:00Z"
    }
  ],
  "totalElements": 10,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 20
}
```

**Curl Example:**
```bash
curl -X GET "http://localhost:8080/api/garlands?page=0&size=20" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 3.2 Get Garlands by Category
**Endpoint:** `GET /garlands/category/{categoryId}?page=0&size=20`
**Access:** Protected (ADMIN, MANAGER, VIEWER)
**Description:** Get all garlands in a specific category

**Path Parameters:**
- `categoryId` (int): Category ID

**Query Parameters:**
- `page` (int, optional): Page number (default: 0)
- `size` (int, optional): Page size (default: 20)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Rose Wedding Garland",
      "productCode": "GARLAND-001",
      "price": 1500.00,
      "description": "Beautiful red rose wedding garland",
      "categoryId": 1,
      "imageId": 201,
      "isAvailable": true,
      "isActive": true,
      "createdAt": "2026-07-03T15:00:00Z",
      "updatedAt": "2026-07-03T20:00:00Z"
    }
  ],
  "totalElements": 5,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 20
}
```

**Curl Example:**
```bash
curl -X GET "http://localhost:8080/api/garlands/category/1?page=0&size=20" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 3.3 Get Single Garland
**Endpoint:** `GET /garlands/{id}`
**Access:** Protected (ADMIN, MANAGER, VIEWER)
**Description:** Get details of a specific garland

**Path Parameters:**
- `id` (int): Garland ID

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Rose Wedding Garland",
  "productCode": "GARLAND-001",
  "price": 1500.00,
  "description": "Beautiful red rose wedding garland with premium quality",
  "orderBy": 1,
  "categoryId": 1,
  "imageId": 201,
  "isAvailable": true,
  "isActive": true,
  "createdAt": "2026-07-03T15:00:00Z",
  "updatedAt": "2026-07-03T20:00:00Z"
}
```

**Curl Example:**
```bash
curl -X GET http://localhost:8080/api/garlands/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 3.4 Create Garland
**Endpoint:** `POST /garlands`
**Access:** Protected (ADMIN only)
**Description:** Create a new garland under a category

**Request Body:**
```json
{
  "name": "Rose Wedding Garland",
  "productCode": "GARLAND-001",
  "price": 1500.00,
  "description": "Beautiful red rose wedding garland",
  "orderBy": 1,
  "categoryId": 1,
  "imageId": 201
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Rose Wedding Garland",
  "productCode": "GARLAND-001",
  "price": 1500.00,
  "description": "Beautiful red rose wedding garland",
  "orderBy": 1,
  "categoryId": 1,
  "imageId": 201,
  "isAvailable": true,
  "isActive": true,
  "createdAt": "2026-07-03T20:34:11Z",
  "updatedAt": "2026-07-03T20:34:11Z"
}
```

**Response (400 Bad Request):**
```json
{
  "error": "Validation failed",
  "details": {
    "price": "Price must be greater than 0",
    "productCode": "Product code must be unique"
  },
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**Curl Example:**
```bash
curl -X POST http://localhost:8080/api/garlands \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Rose Wedding Garland",
    "productCode": "GARLAND-001",
    "price": 1500.00,
    "description": "Beautiful red rose wedding garland",
    "categoryId": 1,
    "imageId": 201
  }'
```

---

### 3.5 Update Garland
**Endpoint:** `PUT /garlands/{id}`
**Access:** Protected (ADMIN only)
**Description:** Update an existing garland

**Path Parameters:**
- `id` (int): Garland ID

**Request Body:**
```json
{
  "name": "Premium Rose Wedding Garland",
  "price": 1800.00,
  "description": "Premium quality wedding garland",
  "isAvailable": true
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Premium Rose Wedding Garland",
  "productCode": "GARLAND-001",
  "price": 1800.00,
  "description": "Premium quality wedding garland",
  "categoryId": 1,
  "imageId": 201,
  "isAvailable": true,
  "isActive": true,
  "createdAt": "2026-07-03T20:34:11Z",
  "updatedAt": "2026-07-03T21:00:00Z"
}
```

**Curl Example:**
```bash
curl -X PUT http://localhost:8080/api/garlands/1 \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Premium Rose Wedding Garland",
    "price": 1800.00,
    "description": "Premium quality wedding garland"
  }'
```

---

### 3.6 Delete Garland
**Endpoint:** `DELETE /garlands/{id}`
**Access:** Protected (ADMIN only)
**Description:** Soft delete a garland (sets is_active to false)

**Path Parameters:**
- `id` (int): Garland ID

**Response (204 No Content):**
No response body

**Curl Example:**
```bash
curl -X DELETE http://localhost:8080/api/garlands/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 4. COMPANY MANAGEMENT ENDPOINTS

### 4.1 Get Company Details
**Endpoint:** `GET /company`
**Access:** Protected (ADMIN, MANAGER, VIEWER)
**Description:** Retrieve company configuration details

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Blossom Garlands",
  "description": "Premium garland supplier for weddings and festivals",
  "shortDescription": "Your trusted garland partner",
  "imageId": 301,
  "address": "123 Flower Street, Garden City",
  "phoneNumber": "+91-9876543210",
  "emailId": "info@blossomgarlands.com",
  "whatsapp": "+91-9876543210",
  "isActive": true,
  "createdAt": "2026-07-03T15:00:00Z",
  "updatedAt": "2026-07-03T20:00:00Z"
}
```

**Curl Example:**
```bash
curl -X GET http://localhost:8080/api/company \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

### 4.2 Update Company Details
**Endpoint:** `PATCH /company`
**Access:** Protected (ADMIN only)
**Description:** Update individual company fields (partial update)

**Request Body (any field combination):**
```json
{
  "name": "Blossom Premium Garlands",
  "description": "Premium garland supplier",
  "phoneNumber": "+91-9876543211"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Blossom Premium Garlands",
  "description": "Premium garland supplier",
  "shortDescription": "Your trusted garland partner",
  "imageId": 301,
  "address": "123 Flower Street, Garden City",
  "phoneNumber": "+91-9876543211",
  "emailId": "info@blossomgarlands.com",
  "whatsapp": "+91-9876543210",
  "isActive": true,
  "createdAt": "2026-07-03T15:00:00Z",
  "updatedAt": "2026-07-03T21:00:00Z"
}
```

**Curl Example:**
```bash
curl -X PATCH http://localhost:8080/api/company \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Blossom Premium Garlands",
    "phoneNumber": "+91-9876543211"
  }'
```

---

### 4.3 Create/Initialize Company
**Endpoint:** `POST /company`
**Access:** Protected (ADMIN only)
**Description:** Create company record (only one allowed, typically done once)

**Request Body:**
```json
{
  "name": "Blossom Garlands",
  "description": "Premium garland supplier",
  "shortDescription": "Your trusted partner",
  "address": "123 Flower Street",
  "phoneNumber": "+91-9876543210",
  "emailId": "info@blossom.com",
  "whatsapp": "+91-9876543210",
  "imageId": 301
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Blossom Garlands",
  "description": "Premium garland supplier",
  "shortDescription": "Your trusted partner",
  "address": "123 Flower Street",
  "phoneNumber": "+91-9876543210",
  "emailId": "info@blossom.com",
  "whatsapp": "+91-9876543210",
  "imageId": 301,
  "isActive": true,
  "createdAt": "2026-07-03T20:34:11Z",
  "updatedAt": "2026-07-03T20:34:11Z"
}
```

**Curl Example:**
```bash
curl -X POST http://localhost:8080/api/company \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Blossom Garlands",
    "description": "Premium garland supplier"
  }'
```

---

## Error Responses

### Common Error Codes

**400 Bad Request:**
```json
{
  "error": "Validation failed",
  "details": {
    "field1": "Error message 1",
    "field2": "Error message 2"
  },
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**401 Unauthorized:**
```json
{
  "error": "Unauthorized - Invalid or expired token",
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**403 Forbidden:**
```json
{
  "error": "Access denied - Insufficient permissions",
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**404 Not Found:**
```json
{
  "error": "Resource not found",
  "timestamp": "2026-07-03T20:34:11Z"
}
```

**500 Internal Server Error:**
```json
{
  "error": "Internal server error",
  "timestamp": "2026-07-03T20:34:11Z"
}
```

---

## Response Status Codes

| Method | Endpoint | Status | Description |
|--------|----------|--------|-------------|
| POST | /auth/login | 200 | Login successful |
| POST | /auth/logout | 200 | Logout successful |
| POST | /auth/forget-password | 200 | Reset link sent |
| GET | /categories | 200 | Categories retrieved |
| POST | /categories | 201 | Category created |
| PUT | /categories/{id} | 200 | Category updated |
| DELETE | /categories/{id} | 204 | Category deleted |
| GET | /garlands | 200 | Garlands retrieved |
| POST | /garlands | 201 | Garland created |
| PUT | /garlands/{id} | 200 | Garland updated |
| DELETE | /garlands/{id} | 204 | Garland deleted |
| GET | /company | 200 | Company details |
| PATCH | /company | 200 | Company updated |
| POST | /company | 201 | Company created |

---

## JWT Token Details

**Token Structure:**
```
header.payload.signature
```

**Payload Example:**
```json
{
  "sub": "1",
  "username": "admin",
  "roles": ["ADMIN"],
  "iat": 1688404451,
  "exp": 1688408051
}
```

**Token Expiry:** 1 hour (3600 seconds)
**Refresh Token Expiry:** 7 days

---

## Rate Limiting & Pagination

### Pagination Parameters:
- Default page size: 20
- Max page size: 100
- Pages are 0-indexed

### Example Paginated Response:
```json
{
  "content": [...],
  "totalElements": 150,
  "totalPages": 8,
  "currentPage": 0,
  "pageSize": 20,
  "hasNextPage": true,
  "hasPreviousPage": false
}
```

---

## Validation Rules

### User Input Validation:
- **Name fields**: Required, 3-150 characters
- **Email**: Valid email format, max 255 characters
- **Password**: Min 8 characters, max 255
- **Price**: Decimal with 2 places, > 0
- **Product Code**: Unique, alphanumeric, 5-50 characters
- **Phone Numbers**: Valid format, max 20 characters

