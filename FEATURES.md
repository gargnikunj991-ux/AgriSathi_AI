# 🌟 AgriSathi AI — Feature Specifications & Capabilities Matrix

**Version:** 1.0  
**Target Audience:** Farmers, Buyers, Agricultural Consultants, System Administrators  

---

## 1. Executive Feature Matrix

| Module | Feature Name | Status | Description |
| :--- | :--- | :---: | :--- |
| **Authentication** | User Registration & Login | ✅ Implemented | Secure sign-up/login with BCrypt password hashing and JWT token issuance. |
| **Authentication** | Current User Profile (`/auth/me`) | ✅ Implemented | Retrieve logged-in session context and role credentials. |
| **Farmer Profile** | Farm & Location Metadata | ✅ Implemented | State, district, village, farm acreage, soil type, and main crop selection. |
| **Crop Lifecycle** | Crop Tracking & Sowing Log | ✅ Implemented | Record sowing date, expected harvest date, and crop status (`PLANTED`, `HARVESTED`, `FAILED`). |
| **Disease Scan** | AI Crop Diagnostic Scan | ✅ Implemented | Upload crop images for automated AI disease detection and remedial measures. |
| **Disease Scan** | Historical Diagnostics Log | ✅ Implemented | Maintain historical records of previous crop scans per farmer. |
| **Marketplace** | Produce Listing Creation | ✅ Implemented | Farmers post produce quantity, unit pricing, location, and status. |
| **Marketplace** | Buyer Produce Search | ✅ Implemented | Search and filter active produce listings by location and crop type. |
| **Government Schemes**| Subsidy & Policy Navigator | ✅ Implemented | Browse federal/state agricultural assistance programs, eligibility, and direct apply links. |
| **AI Assistant** | Advisory Chatbot | ✅ Implemented | Interactive AI assistance for farming queries, pest control advice, and seasonal tips. |
| **Weather Advisory**| Hyper-local Forecast Cache | ✅ Implemented | Fetch and cache temperature, humidity, rainfall, and weather advisories. |
| **File Storage** | Cloud Image Management | ✅ Implemented | Direct cloud upload integration via Cloudinary API. |

---

## 2. Deep Dive into Core Modules

### 2.1. 🔬 AI Crop Disease Diagnostics
- **Image Analysis**: Farmers upload photographs of unhealthy leaves or crops via mobile/web.
- **Disease Recognition**: AI inference model scans the image to identify leaf spot, blight, rust, mildew, or nutrient deficiency.
- **Confidence Scoring**: Returns a confidence percentage for diagnostic precision.
- **Treatment Protocols**: Provides actionable organic and chemical treatment instructions to mitigate crop loss.

### 2.2. 🌾 Personalised Farmer Profile & Farm Intelligence
- **Geo-Contextualization**: Stores state, district, and village information to tailor weather alerts and government scheme recommendations.
- **Soil Profiling**: Classifies farm soil (e.g., Loamy, Clay, Alluvial, Black Soil) to recommend optimal fertilizers and crop rotation cycles.

### 2.3. 🛒 Direct Producer-to-Buyer Marketplace
- **Fair Pricing**: Eliminates middlemen by enabling direct farmer-to-buyer transactions.
- **Listing Lifecycle**: Post listings as `ACTIVE`, update pricing/quantity, or mark as `SOLD`.
- **Search & Discovery**: Allows buyers to filter produce by state, district, crop type, and price range.

### 2.4. 🏛️ Government Schemes Navigator
- **Categorization**: Groups schemes into Subsidies, Organic Farming Support, Crop Insurance (PMFBY), and Equipment Financing.
- **Eligibility Engine**: Displays clear eligibility criteria so farmers can easily find eligible schemes.
- **Direct Application**: Links directly to official portal submission pages.

### 2.5. 🤖 Multilingual AI Advisory Assistant
- **24/7 Availability**: Answers questions on soil preparation, irrigation schedules, fertilizer dosages, and pest management.
- **Context Retention**: Saves user prompt and AI response in `chat_history` for continuous conversation context.

---

## 3. Security & Administrative Features

- **Role-Based Access Control (RBAC)**:
  - `ROLE_USER`: Standard farmer/buyer access to personal profiles, scans, marketplace, and chat.
  - `ROLE_ADMIN`: Administrative permissions to manage government scheme listings, review flagged content, and oversee platform health.
- **Auditing & Timestamps**: Automated tracking of `created_at` and `updated_at` timestamps across all entity records.
