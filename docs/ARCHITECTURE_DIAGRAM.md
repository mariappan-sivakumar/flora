# Architecture Diagram - Garland Admin Portal

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER (Admin UI)                             │
│                       (React / Angular / Vue)                               │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │
                                       │ HTTP/HTTPS Requests
                                       │ JSON Payloads
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    API GATEWAY & SECURITY LAYER                             │
│  ├─ CORS Filter                                                             │
│  ├─ JWT Token Validation                                                    │
│  └─ Exception Handling Filter                                               │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                     CONTROLLER LAYER (REST Endpoints)                       │
│  ┌──────────────────┬──────────────────┬──────────────────┬────────────────┐│
│  │ AuthController   │ CategoryController│ GarlandController│CompanyController││
│  ├──────────────────┼──────────────────┼──────────────────┼────────────────┤│
│  │ • POST /login    │ • GET /list      │ • GET /list      │ • GET /details ││
│  │ • POST /logout   │ • GET /{id}      │ • GET /by-cat    │ • PATCH /update││
│  │ • POST /password │ • POST /create   │ • GET /{id}      │ • POST /create ││
│  │   -reset         │ • PUT /update    │ • POST /create   │ • DELETE       ││
│  │                  │ • DELETE /{id}   │ • PUT /update    │                ││
│  │                  │                  │ • DELETE /{id}   │                ││
│  └──────────────────┴──────────────────┴──────────────────┴────────────────┘│
│                                                                             │
│  Request Validation → Response DTO → HTTP Status                           │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                     SERVICE LAYER (Business Logic)                          │
│  ┌──────────────────┬──────────────────┬──────────────────┬────────────────┐│
│  │ AuthService      │ CategoryService  │ GarlandService   │ CompanyService ││
│  ├──────────────────┼──────────────────┼──────────────────┼────────────────┤│
│  │ • authenticate   │ • listAll        │ • listAll        │ • getDetails   ││
│  │ • generateToken  │ • getById        │ • getByCategory  │ • updateField  ││
│  │ • validateToken  │ • create         │ • getById        │ • create       ││
│  │ • resetPassword  │ • update         │ • create         │ • delete       ││
│  │                  │ • delete         │ • update         │                ││
│  │                  │ • validateData   │ • delete         │                ││
│  └──────────────────┴──────────────────┴──────────────────┴────────────────┘│
│                                                                             │
│  Business logic, validation, transaction management                         │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                  REPOSITORY LAYER (Data Access Object)                      │
│  ┌──────────────────┬──────────────────┬──────────────────┬────────────────┐│
│  │ UserRepository   │ CategoryRepository│ GarlandRepository│CompanyRepository││
│  │ RoleRepository   │ ImageRepository  │                  │ ImageRepository ││
│  │ UserRoleRepo     │                  │                  │                 ││
│  └──────────────────┴──────────────────┴──────────────────┴────────────────┘│
│                                                                             │
│  JpaRepository extending Spring Data JPA                                    │
│  Custom queries for filtering, sorting                                      │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      PERSISTENCE LAYER (JPA/Hibernate)                      │
│  ┌──────────────────────────────────────────────────────────────────────────┐│
│  │                       Entity Classes                                     ││
│  │  • User          • Role           • Category       • Garland            ││
│  │  • UserRole      • Company        • Image                               ││
│  └──────────────────────────────────────────────────────────────────────────┘│
│  ORM Mapping, Lazy Loading, Relationship Management                         │
└──────────────────────────────────────┬──────────────────────────────────────┘
                                       │
                                       ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                        DATABASE LAYER                                       │
│  ┌──────────────────────────────────────────────────────────────────────────┐│
│  │ MySQL / PostgreSQL Database with Liquibase Migrations                   ││
│  │ Tables: user, role, user_role, category, garland, company, image        ││
│  └──────────────────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────────────────┘
```

## Component Interaction Flow

### Authentication Flow
```
┌──────────────┐
│ Admin Client │
└──────┬───────┘
       │ POST /api/auth/login (username, password)
       ▼
┌──────────────────────┐
│ AuthController       │
└──────┬───────────────┘
       │ validate input
       ▼
┌──────────────────────┐
│ AuthService          │
└──────┬───────────────┘
       │ authenticate user
       ▼
┌──────────────────────┐
│ UserRepository       │
└──────┬───────────────┘
       │ find by username
       ▼
┌──────────────────────┐
│ Database             │ ◄────── BCrypt password verification
└──────┬───────────────┘
       │ user found & verified
       ▼
┌──────────────────────────────────────┐
│ Generate JWT Token (include roles)   │
└──────┬───────────────────────────────┘
       │ return JWT + user details
       ▼
┌──────────────────────┐
│ Admin Client         │ ◄────── Token stored in local storage
└──────────────────────┘
       │
       │ Subsequent requests include Authorization: Bearer <token>
       ▼
┌──────────────────────┐
│ JWT Validation Filter│ ◄────── Validate token signature & expiry
└──────────────────────┘
```

### CRUD Operation Flow (Example: Create Garland)
```
┌──────────────────────────────────┐
│ Admin sends POST /api/garlands    │
│ with garland data + JWT token    │
└──────┬───────────────────────────┘
       │
       ▼
┌──────────────────────┐
│ JWT Token Validation │
└──────┬───────────────┘
       │
       ▼
┌──────────────────────┐
│ GarlandController    │
│ extractValidateDTO   │
└──────┬───────────────┘
       │
       ▼
┌──────────────────────┐
│ @Valid @RequestBody  │
│ GarlandCreateDTO     │ ◄────── Bean Validation
└──────┬───────────────┘
       │
       ▼
┌──────────────────────┐
│ GarlandService       │
│ create()             │
└──────┬───────────────┘
       │ business logic validation
       ├─ verify category exists
       ├─ verify product_code unique
       ├─ validate price > 0
       │
       ▼
┌──────────────────────┐
│ CategoryRepository   │
│ getById()            │ ◄────── Fetch category
└──────┬───────────────┘
       │
       ▼
┌──────────────────────┐
│ GarlandRepository    │
│ save()               │ ◄────── Save new garland
└──────┬───────────────┘
       │
       ▼
┌──────────────────────┐
│ Database Transaction │
└──────┬───────────────┘
       │
       ▼
┌──────────────────────┐
│ 201 Created          │
│ GarlandResponseDTO   │ ◄────── Return created resource
└──────────────────────┘
```

## Key Design Patterns

### 1. **Layered Architecture**
- Separation of concerns (Controller → Service → Repository)
- Easy to test each layer independently
- Easier maintenance and scaling

### 2. **DTO Pattern (Data Transfer Object)**
- Separate request/response DTOs from entities
- Avoid exposing internal entity structure
- Validation at API boundary

### 3. **Service Layer**
- Encapsulates business logic
- Transaction management
- Cross-repository operations

### 4. **Repository Pattern**
- Abstraction over data access
- Query methods
- Easy to swap implementations

### 5. **JWT Authentication**
- Stateless authentication
- Token-based authorization
- No session storage needed

## Security Considerations

```
┌─────────────────────────────────────────────────────────────────────┐
│                     SECURITY LAYERS                                │
├─────────────────────────────────────────────────────────────────────┤
│ 1. HTTPS/TLS                                                        │
│    └─ Encrypt data in transit                                       │
│                                                                     │
│ 2. Authentication (JWT)                                             │
│    └─ Verify user identity                                          │
│    └─ Token expiration & refresh                                    │
│                                                                     │
│ 3. Authorization (Role-based)                                       │
│    └─ Role checks via @PreAuthorize                                 │
│    └─ Endpoint access control                                       │
│                                                                     │
│ 4. Input Validation                                                 │
│    └─ @Valid annotations                                            │
│    └─ Custom validators                                             │
│                                                                     │
│ 5. Password Security                                                │
│    └─ BCrypt hashing (10 rounds)                                    │
│    └─ No plaintext storage                                          │
│                                                                     │
│ 6. Database Security                                                │
│    └─ Parameterized queries (JPA prevents SQL injection)            │
│    └─ Principle of least privilege                                  │
│                                                                     │
│ 7. Exception Handling                                               │
│    └─ No sensitive info in error messages                           │
│    └─ Consistent error responses                                    │
└─────────────────────────────────────────────────────────────────────┘
```

## Deployment Architecture

```
┌─────────────────────────────────────────────────────────┐
│              Load Balancer / Reverse Proxy              │
│              (Nginx / HAProxy)                          │
└──────────────────────┬──────────────────────────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
┌──────────────┐┌──────────────┐┌──────────────┐
│ Spring Boot  ││ Spring Boot  ││ Spring Boot  │
│ Instance 1   ││ Instance 2   ││ Instance 3   │
└──────┬───────┘└──────┬───────┘└──────┬───────┘
       │                │                │
       └────────────────┼────────────────┘
                        │
                        ▼
        ┌──────────────────────────────┐
        │   Database Cluster           │
        │   (MySQL / PostgreSQL)       │
        │   with replication           │
        └──────────────────────────────┘
```
