# 🏛️ AgriSathi AI — System Architecture & Component Design

**Version:** 1.0  
**Target Platform:** Cloud Native / On-Premise Microservice Ready  
**Core Framework:** Spring Boot 3.x (Java 21 LTS)  

---

## 1. High-Level Architecture Overview

AgriSathi AI is built on a **Layered Domain-Driven Enterprise Architecture** designed for high throughput, maintainability, and seamless scalability.

```mermaid
graph TD
    UserClient["📱 Client App (Web / Mobile)"] -->|HTTPS / REST API| SecurityFilter["🔒 Spring Security & JWT Filter"]
    
    subgraph Spring Boot Backend ["⚙️ AgriSathi Spring Boot Core Engine"]
        SecurityFilter --> AuthCtrl["AuthController"]
        SecurityFilter --> FarmerCtrl["FarmerProfileController"]
        SecurityFilter --> CropCtrl["CropController"]
        SecurityFilter --> ScanCtrl["DiseaseScanController"]
        SecurityFilter --> MarketCtrl["MarketplaceController"]
        SecurityFilter --> ChatCtrl["ChatController"]
        
        AuthCtrl --> AuthService["AuthService"]
        FarmerCtrl --> FarmerService["FarmerProfileService"]
        CropCtrl --> CropService["CropService"]
        ScanCtrl --> ScanService["DiseaseScanService"]
        MarketCtrl --> MarketService["MarketplaceService"]
        ChatCtrl --> ChatService["ChatService"]
        
        AuthService --> Repositories["Spring Data JPA Repositories"]
        FarmerService --> Repositories
        CropService --> Repositories
        ScanService --> Repositories
        MarketService --> Repositories
        ChatService --> Repositories
    end

    subgraph Data & Storage Layer ["💾 Data & Storage Layer"]
        Repositories -->|JDBC SQL Queries| Database[("🐘 PostgreSQL / MySQL DB")]
    end

    subgraph External Services ["🌐 External Services Integration"]
        ScanService -->|Cloud Storage API| Cloudinary["☁️ Cloudinary Storage"]
        ScanService -->|Vision AI Inference| VisionAI["🧠 Disease Detection Model API"]
        ChatService -->|NLP Prompt / Response| LLMProvider["🤖 GenAI LLM Engine"]
        WeatherCtrl -->|REST API Calls| OpenWeather["🌦️ External Weather API"]
    end
```

---

## 2. Data Flow Architecture

### 2.1. AI Crop Disease Scanning Flow

```mermaid
sequenceDiagram
    autonumber
    actor Farmer as Farmer / User
    participant App as Mobile/Web App
    participant Controller as DiseaseScanController
    participant Service as DiseaseScanService
    participant Cloud as Cloudinary Storage
    participant AI as Disease AI Model API
    participant DB as PostgreSQL DB

    Farmer->>App: Upload Crop Image & Select Crop
    App->>Controller: POST /api/v1/disease-scans (Multipart File)
    Controller->>Service: processScan(user, file, cropId)
    Service->>Cloud: uploadImage(fileBytes)
    Cloud-->>Service: Return Secure Image URL
    Service->>AI: analyzeImage(imageUrl)
    AI-->>Service: Return Disease Name, Confidence Score & Remedies
    Service->>DB: Save DiseaseScan Entity
    DB-->>Service: Saved Scan Entity (ID)
    Service-->>Controller: Return DiseaseScanResponse DTO
    Controller-->>App: 201 Created (ApiResponse<DiseaseScanResponse>)
    App-->>Farmer: Display Diagnosis & Treatment Plan
```

### 2.2. User Authentication & Authorization Flow

```mermaid
sequenceDiagram
    autonumber
    actor User
    participant Client
    participant AuthCtrl as AuthController
    participant AuthMgr as AuthenticationManager
    participant JWTProvider as JwtTokenProvider
    participant DB as Users Table

    User->>Client: Input Email & Password
    Client->>AuthCtrl: POST /api/v1/auth/login
    AuthCtrl->>AuthMgr: authenticate(UsernamePasswordAuthenticationToken)
    AuthMgr->>DB: Fetch User details & BCrypt Check
    DB-->>AuthMgr: Valid User Details
    AuthMgr-->>AuthCtrl: Authenticated UserPrincipal
    AuthCtrl->>JWTProvider: generateToken(UserPrincipal)
    JWTProvider-->>AuthCtrl: Signed JWT Bearer Token
    AuthCtrl-->>Client: 200 OK (AuthTokenResponse DTO)
    Client->>Client: Save JWT in Secure Local Storage
```

---

## 3. Core Component Layers

### 3.1. Web & Security Controller Layer
- Implements strict request validation via `@Valid` annotations and Jakarta Bean Validation constraints.
- Wraps all API responses within a unified `ApiResponse<T>` envelope for standard front-end processing.
- Handles stateless request authentication via custom `JwtAuthenticationFilter`.

### 3.2. Service & Business Domain Layer
- Contains transactional business logic marked with `@Transactional`.
- Decouples domain models from REST request payloads using custom/MapStruct DTO mappers.
- Handles error boundaries by throwing custom runtime exceptions (`ResourceNotFoundException`, `BadRequestException`, `UnauthorizedException`).

### 3.3. Persistence & Repository Layer
- Leverages Spring Data JPA for type-safe repository patterns.
- Includes custom JPQL and native SQL queries optimized with database indexes (`idx_users_email`, `idx_crops_user`, `idx_scans_user`).

---

## 4. Non-Functional Requirements & System Guarantees

| Requirement | Implementation & Metric |
| :--- | :--- |
| **Statelessness** | Zero HTTP session persistence. All client requests carry authorization headers. |
| **Performance** | Database connection pooling via HikariCP (`maximum-pool-size=10`). |
| **Security** | BCrypt password hash strength of 10 rounds; CORS restricted to verified web domains. |
| **Data Integrity** | Cascade policies and foreign key constraints on relational schemas. |
| **Observability** | Centralized exception handling via `GlobalExceptionHandler` with structured timestamps. |
