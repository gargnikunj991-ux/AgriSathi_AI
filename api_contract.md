# AgriSathi AI - API Contract

**Version:** 1.0  
**Backend:** Spring Boot 3.x  
**Base URL:** /api/v1  
**Authentication:** JWT Bearer Token  
**Content-Type:** application/json  

---

# API Standards

## Authentication

Protected endpoints require:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

## Success Response Format

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {}
}
```

---

## Error Response Format

```json
{
  "success": false,
  "message": "Validation Failed",
  "errors": [
    "Crop name is required"
  ],
  "timestamp": "2026-07-28T15:45:21"
}
```

---

## HTTP Status Codes

| Status | Meaning |
|---|---|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 500 | Internal Server Error |

---

# Authentication APIs

## Register
`POST /api/v1/auth/register`

Authentication Required: No

### Request
```json
{
  "name": "Nick",
  "email": "nick@gmail.com",
  "password": "Password@123",
  "phone": "9876543210"
}
```

### Response
```json
{
  "success": true,
  "message": "User registered successfully"
}
```

---

## Login
`POST /api/v1/auth/login`

Authentication Required: No

### Request
```json
{
  "email": "nick@gmail.com",
  "password": "Password@123"
}
```

### Response
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "JWT_TOKEN",
    "expiresIn": 1800
  }
}
```

---

## Get Logged-in User
`GET /api/v1/auth/me`

Authentication Required: Yes

---

# Farmer Profile APIs

## Create / Update Profile
`PUT /api/v1/farmer/profile`

### Request
```json
{
  "state": "Uttarakhand",
  "district": "Dehradun",
  "village": "Raipur",
  "farmSize": 2.5,
  "soilType": "Loamy",
  "mainCrop": "Rice"
}
```

### Response
```json
{
  "success": true,
  "message": "Profile Updated Successfully"
}
```

---

## Get Profile
`GET /api/v1/farmer/profile`

---

# Crop APIs

## Add Crop
`POST /api/v1/crops`

### Request
```json
{
  "cropName": "Rice",
  "sowingDate": "2026-07-30",
  "harvestDate": "2026-11-15"
}
```

---

## Get All Crops
`GET /api/v1/crops`

---

## Get Crop
`GET /api/v1/crops/{cropId}`

---

## Update Crop
`PUT /api/v1/crops/{cropId}`

---

## Delete Crop
`DELETE /api/v1/crops/{cropId}`

---

# Disease Detection APIs

## Upload Crop Image & Scan
`POST /api/v1/disease/scan`  
Content-Type: `multipart/form-data`

### Body
- `image`: File

### Response
```json
{
  "success": true,
  "data": {
    "disease": "Leaf Rust",
    "confidence": 98.2,
    "treatment": "Copper Fungicide"
  }
}
```

---

## Scan History
`GET /api/v1/disease/history`

---

# Weather APIs

## Current Weather
`GET /api/v1/weather?lat=30.3165&lon=78.0322`

### Response
```json
{
  "temperature": 31,
  "humidity": 64,
  "windSpeed": 11,
  "forecast": "Rain Expected"
}
```

---

# Fertilizer Recommendation APIs

## Recommend Fertilizer
`POST /api/v1/recommendations/fertilizer`

### Request
```json
{
  "crop": "Rice",
  "soilType": "Loamy",
  "disease": "Leaf Rust"
}
```

### Response
```json
{
  "fertilizer": "NPK 20:20:20",
  "quantity": "50kg/hectare"
}
```

---

# AI Chat APIs

## Chat with AI
`POST /api/v1/chat`

### Request
```json
{
  "message": "How do I treat leaf rust?"
}
```

### Response
```json
{
  "response": "Leaf rust can be treated using..."
}
```

---

# Marketplace APIs

## Create Listing
`POST /api/v1/marketplace/listings`

### Request
```json
{
  "cropName": "Rice",
  "quantity": 250,
  "price": 35,
  "unit": "kg",
  "location": "Dehradun"
}
```

---

## Get All Listings
`GET /api/v1/marketplace/listings`

---

## Get Listing
`GET /api/v1/marketplace/listings/{listingId}`

---

## Update Listing
`PUT /api/v1/marketplace/listings/{listingId}`

---

## Delete Listing
`DELETE /api/v1/marketplace/listings/{listingId}`

---

## My Listings
`GET /api/v1/marketplace/my-listings`

---

# Government Schemes APIs

## Get Schemes
`GET /api/v1/government-schemes?state=Uttarakhand`

---

# File Upload APIs

`POST /api/v1/files/upload`  
Content-Type: `multipart/form-data`

### Response
```json
{
  "url": "https://cloudinary.com/xxxxx.jpg"
}
```

---

# Validation & Security Rules

- **Password**: Min 8 chars, 1 uppercase, 1 lowercase, 1 number, 1 special char.
- **Phone**: 10 digits.
- **Email**: Valid RFC 5322 email syntax.
- **Crop Name**: Max 100 chars.
- **Security**: JWT Auth, BCrypt Encoding, CORS Protection, Input Validation, HTTPS in production.
