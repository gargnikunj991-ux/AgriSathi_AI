# AgriSathi AI - Project Structure & Architectural Guidelines

**Version:** 1.0  
**Language / Framework:** Java 17+ / Spring Boot 3.x  
**Build Tool:** Maven / Gradle  
**Architecture Pattern:** Layered Domain-Driven Architecture (Controller-Service-Repository)  

---

## 1. Architectural Blueprint

AgriSathi AI utilizes a traditional **Layered Enterprise Architecture** with strict separation of concerns to maintain testability, scalability, and maintainability.

```
+-------------------------------------------------------+
|                HTTP Client / Frontend                 |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|  Controller Layer (REST Endpoints & Validation DTOs)  |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|   Service Layer (Business Logic & Transactions)      |
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|  Repository Layer (Spring Data JPA / Database Queries)|
+-------------------------------------------------------+
                           |
                           v
+-------------------------------------------------------+
|           Database (PostgreSQL / MySQL)               |
+-------------------------------------------------------+
```

---

## 2. Directory & Package Directory Tree

```
agrisathi-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── agrisathi/
│   │   │           └── api/
│   │   │               ├── AgriSathiApplication.java
│   │   │               │
│   │   │               ├── config/                    # System & Security Configuration
│   │   │               │   ├── CorsConfig.java
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── SwaggerConfig.java
│   │   │               │   └── RestTemplateConfig.java
│   │   │               │
│   │   │               ├── security/                  # JWT & Auth Filters
│   │   │               │   ├── JwtAuthenticationEntryPoint.java
│   │   │               │   ├── JwtAuthenticationFilter.java
│   │   │               │   ├── JwtTokenProvider.java
│   │   │               │   └── UserDetailsServiceImpl.java
│   │   │               │
│   │   │               ├── controller/                # REST Controllers
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── FarmerProfileController.java
│   │   │               │   ├── CropController.java
│   │   │               │   ├── DiseaseScanController.java
│   │   │               │   ├── WeatherController.java
│   │   │               │   ├── RecommendationController.java
│   │   │               │   ├── ChatController.java
│   │   │               │   ├── MarketplaceController.java
│   │   │               │   ├── GovernmentSchemeController.java
│   │   │               │   └── FileUploadController.java
│   │   │               │
│   │   │               ├── service/                   # Business Service Interfaces
│   │   │               │   ├── AuthService.java
│   │   │               │   ├── FarmerProfileService.java
│   │   │               │   ├── CropService.java
│   │   │               │   ├── DiseaseScanService.java
│   │   │               │   ├── WeatherService.java
│   │   │               │   ├── RecommendationService.java
│   │   │               │   ├── ChatService.java
│   │   │               │   ├── MarketplaceService.java
│   │   │               │   ├── GovernmentSchemeService.java
│   │   │               │   └── FileStorageService.java
│   │   │               │   └── impl/                  # Service Implementations
│   │   │               │       ├── AuthServiceImpl.java
│   │   │               │       ├── CropServiceImpl.java
│   │   │               │       └── ...
│   │   │               │
│   │   │               ├── repository/                # Spring Data JPA Repositories
│   │   │               │   ├── UserRepository.java
│   │   │               │   ├── FarmerProfileRepository.java
│   │   │               │   ├── CropRepository.java
│   │   │               │   ├── DiseaseScanRepository.java
│   │   │               │   ├── MarketplaceListingRepository.java
│   │   │               │   ├── ChatHistoryRepository.java
│   │   │               │   └── GovernmentSchemeRepository.java
│   │   │               │
│   │   │               ├── model/                     # Data Models
│   │   │               │   ├── entity/                # JPA Entities
│   │   │               │   │   ├── User.java
│   │   │               │   │   ├── FarmerProfile.java
│   │   │               │   │   ├── Crop.java
│   │   │               │   │   ├── DiseaseScan.java
│   │   │               │   │   ├── MarketplaceListing.java
│   │   │               │   │   ├── ChatHistory.java
│   │   │               │   │   ├── GovernmentScheme.java
│   │   │               │   │   └── UploadedFile.java
│   │   │               │   └── enums/                 # Application Enums
│   │   │               │       ├── Role.java
│   │   │               │       └── ListingStatus.java
│   │   │               │
│   │   │               ├── dto/                       # Data Transfer Objects
│   │   │               │   ├── request/               # Incoming Request Payloads
│   │   │               │   │   ├── LoginRequest.java
│   │   │               │   │   ├── RegisterRequest.java
│   │   │               │   │   ├── CropRequest.java
│   │   │               │   │   ├── MarketplaceRequest.java
│   │   │               │   │   └── ChatRequest.java
│   │   │               │   ├── response/              # Outgoing Response Payloads
│   │   │               │   │   ├── ApiResponse.java
│   │   │               │   │   ├── AuthTokenResponse.java
│   │   │               │   │   ├── DiseaseScanResponse.java
│   │   │               │   │   └── WeatherResponse.java
│   │   │               │
│   │   │               ├── exception/                 # Custom Exception Handlers
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── ResourceNotFoundException.java
│   │   │               │   ├── BadRequestException.java
│   │   │               │   └── UnauthorizedException.java
│   │   │               │
│   │   │               └── util/                      # Utilities & External Clients
│   │   │                   ├── CloudinaryUtil.java
│   │   │                   └── ExternalWeatherClient.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml                        # Common Config
│   │       ├── application-dev.yml                    # Local Dev Environment Config
│   │       └── application-prod.yml                   # Production Secrets & Config
│   │
│   └── test/                                          # Unit & Integration Tests
│       └── java/
│           └── com/agrisathi/api/
│               ├── controller/
│               └── service/
├── pom.xml                                            # Maven Build Dependencies
├── README.md
└── .gitignore
```

---

## 3. Naming Conventions & Rules

### 3.1. Java Code Conventions
- **Classes / Interfaces:** `PascalCase` (e.g., `CropServiceImpl`, `DiseaseScanRepository`)
- **Methods / Variables:** `camelCase` (e.g., `sowingDate`, `getUserProfile()`)
- **Constants:** `UPPER_SNAKE_CASE` (e.g., `DEFAULT_PAGE_SIZE`, `JWT_HEADER`)
- **Packages:** Lowercase single-word or dot-separated (e.g., `com.agrisathi.api.controller`)

### 3.2. REST API Endpoint Conventions
- **Resource URIs:** Plural nouns in `kebab-case` (e.g., `/api/v1/government-schemes`, `/api/v1/marketplace/listings`)
- **HTTP Methods:**
  - `GET`: Fetch resources (Idempotent)
  - `POST`: Create new entity / Perform action
  - `PUT`: Update existing resource completely
  - `DELETE`: Remove resource

### 3.3. Database Conventions
- **Tables:** Plural `snake_case` (e.g., `farmer_profiles`, `disease_scans`)
- **Columns:** Singular `snake_case` (e.g., `sowing_date`, `password_hash`)
- **Foreign Keys:** Entity name + `_id` (e.g., `user_id`, `crop_id`)

---

## 4. Standard Response Envelope Design

To maintain strict consistency across all backend endpoints, controllers MUST wrap data in the standardized `ApiResponse<T>` DTO:

```java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, List<String> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
```

---

## 5. Coding Standards & Best Practices

1. **Lombok Integration:** Use `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, and `@Builder` to minimize boilerplate.
2. **Dependency Injection:** Use `@RequiredArgsConstructor` with `private final` fields (Constructor Injection) instead of `@Autowired` field injection.
3. **Transactional Management:** Annotate all write operations in the Service layer with `@Transactional`. Use `@Transactional(readOnly = true)` for read methods.
4. **DTO-Entity Decoupling:** Database JPA entities must never be exposed directly via Controller endpoints. Map Entities to DTOs before returning responses.
5. **Environment Configuration:** Never hardcode credentials, passwords, or secret keys in Java files. Store them in environment variables or `application.yml`.
