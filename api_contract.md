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
  "name": "Ramesh Kumar",
  "email": "ramesh@agrisathi.com",
  "password": "Password@123",
  "phone": "9876543210",
  "role": "FARMER"
}
```
> **Note:** `role` is mandatory during registration and must be either `FARMER` or `BUYER`.

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
  "email": "ramesh@agrisathi.com",
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

## Current Weather Overview (Legacy)
`GET /api/v1/weather?lat=30.3165&lon=78.0322`

Authentication Required: No

### Response
```json
{
  "success": true,
  "message": "Weather forecast retrieved successfully",
  "data": {
    "temperature": 30.5,
    "humidity": 64,
    "windSpeed": 11.0,
    "forecast": "Partly Cloudy"
  }
}
```

---

## Detailed Current Weather
`GET /api/v1/weather/current?lat=30.3165&lon=78.0322`

Authentication Required: No

### Response
```json
{
  "success": true,
  "message": "Current weather retrieved successfully",
  "data": {
    "latitude": 30.3165,
    "longitude": 78.0322,
    "temperature": 31.0,
    "apparentTemperature": 33.5,
    "humidity": 64,
    "windSpeed": 11.0,
    "windDirection": 180.0,
    "pressure": 1013.25,
    "precipitation": 0.0,
    "weatherCondition": "Clear sky",
    "weatherCode": 0,
    "uvIndex": 6.0,
    "forecast": "Clear & Sunny",
    "timestamp": "2026-08-02T14:30:00"
  }
}
```

---

## Weather Forecast
`GET /api/v1/weather/forecast?lat=30.3165&lon=78.0322&days=7`

Authentication Required: No

### Response
```json
{
  "success": true,
  "message": "Weather forecast retrieved successfully",
  "data": {
    "latitude": 30.3165,
    "longitude": 78.0322,
    "timezone": "Asia/Kolkata",
    "days": 7,
    "dailyForecasts": [
      {
        "date": "2026-08-02",
        "tempMin": 22.0,
        "tempMax": 33.0,
        "precipitationProbability": 20,
        "precipitation": 0.0,
        "windSpeedMax": 14.0,
        "weatherCondition": "Partly cloudy",
        "weatherCode": 2,
        "uvIndexMax": 7.0
      }
    ]
  }
}
```

---

## Farming Weather Summary
`GET /api/v1/weather/farming-summary?lat=30.3165&lon=78.0322`

Authentication Required: No

### Response
```json
{
  "success": true,
  "message": "Farming weather summary retrieved successfully",
  "data": {
    "latitude": 30.3165,
    "longitude": 78.0322,
    "currentWeather": {
      "temperature": 31.0,
      "humidity": 64,
      "windSpeed": 11.0,
      "precipitation": 0.0,
      "weatherCondition": "Clear sky"
    },
    "overallAdvisory": "Current conditions: 31.0°C, 64% humidity, 11.0 km/h wind. 7-day expected precipitation: 0.0 mm.",
    "irrigationAdvice": "Standard irrigation schedule: Maintain normal watering routines for standing crops.",
    "sprayingCondition": "Favorable: Moderate winds and dry atmosphere provide safe spraying conditions.",
    "frostRisk": "Low Risk: Temperatures will remain safely above freezing threshold.",
    "heatStressRisk": "Low Risk: Temperatures are within healthy crop growth thresholds.",
    "sowingSuitability": "Optimal: Soil temperature and moisture conditions support healthy seed germination.",
    "harvestingSuitability": "Optimal: Dry weather conditions facilitate efficient crop harvesting and drying.",
    "actionItems": [
      "Good window for preventive pesticide/nutrient foliar spray.",
      "Monitor crops regularly and follow standard seasonal field practices."
    ]
  }
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

Authentication Required: Yes

### Request
```json
{
  "cropName": "Rice",
  "description": "Premium organic Basmati rice harvested in Dehradun valley.",
  "imageUrl": "https://cloudinary.com/sample_rice.jpg",
  "quantity": 250,
  "price": 35,
  "unit": "kg",
  "location": "Dehradun"
}
```

### Response
```json
{
  "success": true,
  "message": "Listing created successfully",
  "data": {
    "id": 1,
    "cropName": "Rice",
    "description": "Premium organic Basmati rice...",
    "quantity": 250.0,
    "price": 35.0,
    "unit": "kg",
    "location": "Dehradun",
    "status": "AVAILABLE"
  }
}
```

---

## Browse Listings
`GET /api/v1/marketplace/listings?cropName=Rice&location=Dehradun&minPrice=20&maxPrice=50`

Authentication Required: No

### Request Parameters
- `cropName` (optional): Filter by crop name
- `location` (optional): Filter by location / district
- `minPrice` (optional): Minimum price threshold
- `maxPrice` (optional): Maximum price threshold

---

## Search Listings
`GET /api/v1/marketplace/listings/search?query=Organic+Wheat`

Authentication Required: No

---

## Get Single Listing Details
`GET /api/v1/marketplace/listings/{listingId}`

Authentication Required: No

---

## My Listings
`GET /api/v1/marketplace/my-listings`

Authentication Required: Yes

---

## Update Listing
`PUT /api/v1/marketplace/listings/{listingId}`

Authentication Required: Yes (Must be listing owner)

---

## Delete Listing
`DELETE /api/v1/marketplace/listings/{listingId}`

Authentication Required: Yes (Must be listing owner)

---

## Contact Seller
`POST /api/v1/marketplace/listings/{listingId}/contact`

Authentication Required: Optional

### Request
```json
{
  "message": "Interested in purchasing 100kg of Basmati Rice. Please call me.",
  "buyerName": "John Doe",
  "buyerPhone": "9876543210",
  "buyerEmail": "john@gmail.com"
}
```

### Response
```json
{
  "success": true,
  "message": "Seller contact details and inquiry generated successfully",
  "data": {
    "listingId": 1,
    "cropName": "Rice",
    "quantity": 250.0,
    "price": 35.0,
    "unit": "kg",
    "location": "Dehradun",
    "sellerName": "Ramesh Kumar",
    "sellerPhone": "9998887770",
    "sellerEmail": "ramesh@agrisathi.com",
    "contactStatus": "INQUIRY_SENT",
    "formattedInquiry": "Hi Ramesh Kumar, John Doe is interested in your listing 'Rice'..."
  }
}
```

## Buy Listing
`POST /api/v1/marketplace/listings/{listingId}/buy`

Authentication Required: Yes (`ROLE_BUYER`, `ROLE_FARMER`, `ROLE_ADMIN`)

### Request
```json
{
  "message": "Placing order to buy 100kg Basmati Rice.",
  "buyerName": "Anil Sharma",
  "buyerPhone": "9876543210",
  "buyerEmail": "anil@gmail.com"
}
```

### Response
```json
{
  "success": true,
  "message": "Purchase order submitted successfully",
  "data": {
    "listingId": 1,
    "cropName": "Rice",
    "quantity": 100.0,
    "price": 35.0,
    "unit": "kg",
    "location": "Dehradun",
    "sellerName": "Ramesh Kumar",
    "sellerPhone": "9998887770",
    "sellerEmail": "ramesh@agrisathi.com",
    "contactStatus": "PURCHASE_REQUESTED",
    "formattedInquiry": "Purchase Order Confirmed: Anil Sharma has placed an order to buy produce from listing 'Rice'..."
  }
}
```

## Borrow / Rent Item
`POST /api/v1/marketplace/listings/{listingId}/borrow`

Authentication Required: Yes (`ROLE_BUYER`, `ROLE_FARMER`, `ROLE_ADMIN`)

### Request
```json
{
  "message": "Requesting to borrow tractor equipment for 3 days.",
  "buyerName": "Anil Sharma",
  "buyerPhone": "9876543210",
  "buyerEmail": "anil@gmail.com"
}
```

### Response
```json
{
  "success": true,
  "message": "Borrow request submitted successfully",
  "data": {
    "listingId": 1,
    "cropName": "Tractor Equipment",
    "quantity": 1.0,
    "price": 500.0,
    "unit": "day",
    "location": "Dehradun",
    "sellerName": "Ramesh Kumar",
    "sellerPhone": "9998887770",
    "sellerEmail": "ramesh@agrisathi.com",
    "contactStatus": "BORROW_REQUESTED",
    "formattedInquiry": "Borrow/Rental Request: Anil Sharma has requested to borrow item from listing 'Tractor Equipment'..."
  }
}
```

# Government Schemes APIs

## List & Filter Schemes
`GET /api/v1/government-schemes?state=Uttarakhand&crop=Rice`

Authentication Required: No

### Request Parameters
- `state` (optional): Filter schemes by state (e.g. `Uttarakhand`, `Punjab`, `All India`)
- `crop` (optional): Filter schemes by crop (e.g. `Rice`, `Wheat`, `Commercial Crops`)

### Response
```json
{
  "success": true,
  "message": "Government schemes retrieved successfully",
  "data": [
    {
      "id": 1,
      "title": "Pradhan Mantri Kisan Samman Nidhi (PM-KISAN)",
      "description": "Direct income support of ₹6,000 per year...",
      "state": "All India",
      "targetCrop": "All Crops",
      "eligibility": "All landholding farmer families...",
      "category": "Income Support / Subsidy",
      "benefitAmount": "₹6,000 / year",
      "minFarmSize": 0.1,
      "maxFarmSize": 50.0,
      "applyLink": "https://pmkisan.gov.in/",
      "isActive": true
    }
  ]
}
```

---

## Get Scheme by ID
`GET /api/v1/government-schemes/{id}`

Authentication Required: No

### Response
```json
{
  "success": true,
  "message": "Government scheme retrieved successfully",
  "data": {
    "id": 1,
    "title": "Pradhan Mantri Fasal Bima Yojana (PMFBY)",
    "description": "Comprehensive crop insurance...",
    "state": "All India",
    "targetCrop": "Rice, Wheat, Pulses",
    "category": "Crop Insurance",
    "benefitAmount": "Up to 100% Sum Insured",
    "applyLink": "https://pmfby.gov.in/"
  }
}
```

---

## Recommend Schemes for Farmer
`GET /api/v1/government-schemes/recommendations?state=Uttarakhand&crop=Rice&farmSize=2.5`

Authentication Required: Optional (Uses authenticated user's profile if available, or query parameters)

### Response
```json
{
  "success": true,
  "message": "Government scheme recommendations retrieved successfully",
  "data": [
    {
      "scheme": {
        "id": 5,
        "title": "Uttarakhand State Hill Horticulture & Polyhouse Subsidy Scheme",
        "state": "Uttarakhand",
        "targetCrop": "Fruits, Vegetables, Flowers",
        "benefitAmount": "Up to 80% Polyhouse Construction Subsidy"
      },
      "matchScore": 100,
      "matchReason": "State Match (Uttarakhand) | Specific Crop Match (Rice) | Eligible for Farm Size (2.5 Acres)"
    }
  ]
}
```

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
