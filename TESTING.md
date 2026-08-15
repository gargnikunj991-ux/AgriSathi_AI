# 🧪 AgriSathi AI — Testing Strategy & API Verification Checklist

**Version:** 1.0  
**Test Frameworks:** JUnit 5, Mockito, Spring Boot Test, Postman / cURL  

---

## 1. Testing Framework Overview

The AgriSathi AI backend utilizes JUnit 5 and Mockito for isolated unit tests and Spring Boot Test for context integration testing.

```
agrisathi-backend/src/test/java/com/agrisathi/api/
├── controller/            # REST API Controller MockMvc Tests
├── service/               # Business Logic Unit Tests with Mockito
└── integration/           # Database & End-to-End API Tests
```

---

## 2. Running Automated Tests

### 2.1. Run All Unit & Integration Tests
```bash
cd agrisathi-backend
mvn test
```

### 2.2. Run Specific Test Class
```bash
mvn test -Dtest=AuthServiceTest
```

---

## 3. Core API Endpoint Test Suite

### 3.1. Authentication APIs

#### 1. Register User (`POST /api/v1/auth/register`)
- [x] **Success (201 Created)**: Valid name, email, password, and phone returns `ApiResponse.success("User registered successfully")`.
- [x] **Failure (400 Bad Request)**: Invalid email format or password less than 6 characters returns validation error payload.
- [x] **Failure (409 Conflict)**: Duplicate email submission returns resource conflict exception.

#### 2. Login User (`POST /api/v1/auth/login`)
- [x] **Success (200 OK)**: Correct credentials return JWT `accessToken` and expiration time in seconds (`1800`).
- [x] **Failure (401 Unauthorized)**: Incorrect password returns invalid credentials message.

---

### 3.2. Farmer Profile APIs

#### 1. Create/Update Profile (`PUT /api/v1/farmer/profile`)
- [x] **Success (200 OK)**: Valid state, district, farm size, soil type updates farmer profile.
- [x] **Failure (401 Unauthorized)**: Missing `Authorization: Bearer <TOKEN>` header blocks request.

---

### 3.3. Disease Diagnosis APIs

#### 1. Upload Disease Scan (`POST /api/v1/disease-scans`)
- [x] **Success (201 Created)**: Valid leaf image file uploads to Cloudinary, triggers model evaluation, and saves scan record.
- [x] **Failure (400 Bad Request)**: Unsupported file type (e.g., `.txt`, `.pdf`) or empty file rejected.

---

## 4. Sample Test Requests (cURL Scripts)

### Register User
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Farmer",
    "email": "farmer@agrisathi.app",
    "password": "Password@123",
    "phone": "9876543210"
  }'
```

### Login User
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "farmer@agrisathi.app",
    "password": "Password@123"
  }'
```

### Get Current User Details
```bash
curl -X GET http://localhost:8080/api/v1/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

---

## 5. Pre-Commit Quality Checklist

Before submitting code changes, ensure:
1. `mvn compile` passes with 0 errors.
2. `mvn test` completes with 0 failing assertions.
3. No hardcoded credentials exist in any test configuration files.
