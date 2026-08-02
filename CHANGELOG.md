# 📜 AgriSathi AI — Changelog

All notable changes to the AgriSathi AI platform will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2026-08-02

### ✨ Added
- **Core Architecture**:
  - Initialized Spring Boot 3.x backend application on Java 21 LTS.
  - Implemented Layered Enterprise Architecture (Controller, Service, Repository, Entity, DTO).
  - Configured PostgreSQL / MySQL relational database persistence with Spring Data JPA.
- **Authentication & Security**:
  - Stateless JWT token-based authentication mechanism (`JwtTokenProvider`, `JwtAuthenticationFilter`).
  - Password encryption using BCrypt algorithm (`BCryptPasswordEncoder`).
  - Configured CORS policy (`CorsConfig.java`) and public/private endpoint security rules.
- **Farmer Profile Module**:
  - CRUD operations for farmer profile location (State, District, Village) and agricultural attributes (Farm Size, Soil Type, Main Crop).
- **Crop Lifecycle Module**:
  - Crop tracking features (sowing dates, expected harvest, status updates).
- **AI Disease Scan Module**:
  - Image submission endpoint integrated with Cloudinary cloud storage.
  - Disease diagnosis response engine returning disease label, confidence level, and treatment protocol.
- **Marketplace Module**:
  - Direct producer listing posting, price quotation, quantity updates, and active listing searches.
- **Government Schemes Navigator**:
  - Cataloging and searching federal and state agricultural subsidies and welfare schemes.
- **AI Conversational Assistant**:
  - Interactive advisory chat module preserving conversation log in `chat_history`.
- **Weather Module (Phase 9)**:
  - Integrated Open-Meteo REST API client (`ExternalWeatherClient`) with fallback resilience for real-time weather retrieval.
  - Implemented `/api/v1/weather/current` endpoint for current temperature, humidity, wind, UV index, and precipitation.
  - Implemented `/api/v1/weather/forecast` endpoint for 7-day daily forecasts with temperature ranges and rain probabilities.
  - Implemented `/api/v1/weather/farming-summary` endpoint for agricultural advisories (irrigation advice, spraying conditions, frost risk, heat stress risk, sowing/harvesting suitability, and action items).
  - Spatial weather caching repository (`WeatherCacheRepository`) to minimize external API latency.
- **Government Schemes Module (Phase 10)**:
  - Enhanced `GovernmentScheme` entity with `targetCrop`, `benefitAmount`, `minFarmSize`, `maxFarmSize`, and multi-column indexes.
  - Implemented `/api/v1/government-schemes` endpoint supporting filtering by `state` and `crop`.
  - Implemented `/api/v1/government-schemes/{id}` for single scheme retrieval.
  - Implemented `/api/v1/government-schemes/recommendations` algorithm providing contextual scheme match scoring (0-100%) and match rationale based on state, crop, and farm size.
  - Admin CRUD management endpoints (`POST`, `PUT`, `DELETE /api/v1/government-schemes`).
- **Marketplace Module (Phase 11)**:
  - Enhanced `MarketplaceListing` entity with `description`, `imageUrl`, and location indexes.
  - Implemented `/api/v1/marketplace/listings` (Browse listings with optional `cropName`, `location`, `minPrice`, `maxPrice` filters).
  - Implemented `/api/v1/marketplace/listings/search` (Keyword search across crop, location, and description).
  - Implemented `/api/v1/marketplace/listings` (Create listing), `PUT /api/v1/marketplace/listings/{id}` (Update listing), and `DELETE /api/v1/marketplace/listings/{id}` (Delete listing).
  - Implemented `/api/v1/marketplace/listings/{id}/contact` (Seller contact endpoint returning seller contact info & generated buyer inquiry).
- **System Documentation**:
  - Detailed project documentation (`README.md`, `ARCHITECTURE.md`, `DATABASE_SCHEMA.md`, `PROJECT_STRUCTURE.md`, `API_CONTRACT.md`, `SECURITY.md`, `DEPLOYMENT.md`, `TESTING.md`, `FEATURES.md`, `CONTRIBUTING.md`, `ROADMAP.md`, `LICENSE`).

---

## [0.1.0-alpha] - 2026-07-25
- Initial project prototype creation and initial database entity design.
