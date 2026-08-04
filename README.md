# 🌾 AgriSathi AI — Smart Agricultural Intelligence & Decision Support System

[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)](https://github.com/gargnikunj991-ux/AgriSathi_AI)
[![Java Version](https://img.shields.io/badge/Java-21%2B-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 📌 About AgriSathi AI

**AgriSathi AI** is a comprehensive, enterprise-grade AI-powered agricultural advisory and management platform designed to empower farmers, agricultural extension workers, and produce buyers. 

By integrating modern artificial intelligence, satellite/weather intelligence, and direct marketplace capabilities, AgriSathi AI helps farmers identify crop diseases early, maximize crop yields, discover fair market prices, and easily access government welfare schemes.

---

## ✨ Key Features

- **🔬 AI Crop Disease Diagnosis**: Upload crop image scans for instant AI-based disease detection, confidence scores, and targeted treatment recommendations.
- **👨‍🌾 Personalised Farmer Profiles**: Farm location, soil classification, farm acreage, and primary crop tracking for customized insights.
- **🌱 Active Crop Lifecycle Management**: Track sowing dates, growth stages, harvest schedules, and historical yield data.
- **🌦️ Localized Weather & Advisory**: Hyper-local weather forecasting and automated actionable farming alerts.
- **🛒 Direct Producer-to-Buyer Marketplace**: Peer-to-peer marketplace allowing farmers to list produce with real-time pricing and transparent negotiations.
- **🏛️ Government Schemes Navigator**: Smart categorization and search for federal/state agricultural subsidies, loans, and support schemes.
- **💬 Multilingual AI Advisory Assistant**: Conversational AI chatbot for 24/7 agricultural queries and troubleshooting.
- **🔒 Enterprise Security & Roles**: JWT-based stateless authentication, BCrypt password encryption, and Role-Based Access Control (RBAC).

---

## 🛠️ Technology Stack

| Layer | Technology / Framework |
| :--- | :--- |
| **Language & Runtime** | Java 21 LTS |
| **Framework** | Spring Boot 3.x (Spring Web, Spring Security, Spring Data JPA) |
| **Database** | PostgreSQL 15+ / MySQL 8.0+ |
| **Security & Auth** | JSON Web Tokens (JWT), BCrypt Hashing |
| **Build System** | Apache Maven 3.8+ |
| **Media Storage** | Cloudinary API Integration |
| **API Documentation** | OpenAPI 3.0 / Swagger UI |
| **External APIs** | Weather Data API, AI Computer Vision Service |

---

## 📂 Project Architecture & Quick Links

- [📁 `PROJECT_STRUCTURE.md`](PROJECT_STRUCTURE.md) — Detailed folder tree, packaging strategy, and coding standards.
- [🗄️ `DATABASE_SCHEMA.md`](DATABASE_SCHEMA.md) — ER diagrams, table definitions, relational constraints, and indexes.
- [🔌 `API_CONTRACT.md`](api_contract.md) — Comprehensive REST API endpoint definitions, request/response formats, and status codes.
- [🔐 `SECURITY.md`](SECURITY.md) — Authentication flow, JWT configuration, password policies, and CORS configuration.
- [🏛️ `ARCHITECTURE.md`](ARCHITECTURE.md) — High-level system architecture and data flow diagrams.
- [🌟 `FEATURES.md`](FEATURES.md) — Detailed overview of implemented and upcoming platform features.
- [🚀 `DEPLOYMENT.md`](DEPLOYMENT.md) — Step-by-step production and staging deployment guidelines.
- [🤝 `CONTRIBUTING.md`](CONTRIBUTING.md) — Contribution guidelines, branching model, and code formatting rules.
- [🧪 `TESTING.md`](TESTING.md) — Unit testing checklist, Postman collections, and integration test plan.
- [🗺️ `ROADMAP.md`](ROADMAP.md) — Product evolution strategy and feature rollout timeline.

---

## 🚀 Quick Start Guide

### Prerequisites
- **JDK 21** or higher installed (`java -version`)
- **Maven 3.8+** installed (`mvn -version`)
- **PostgreSQL 15+** or **MySQL 8.0+** running instance

### 1. Clone Repository
```bash
git clone https://github.com/gargnikunj991-ux/AgriSathi_AI.git
cd AgriSathi_AI/agrisathi-backend
```

### 2. Configure Environment Variables
Copy the `.env.example` template to `.env` inside `agrisathi-backend`:
```bash
cp .env.example .env
```
Fill in your database credentials and secret keys:
```properties
PORT=8080
DB_URL=jdbc:postgresql://localhost:5432/agrisathidb
DB_USERNAME=your_postgres_user
DB_PASSWORD=your_postgres_password
JWT_SECRET=your_super_secret_64_character_hex_key
```

### 3. Build & Run Application
```bash
mvn clean install
mvn spring-boot:run
```

The backend server will launch at: `http://localhost:8080`  
Swagger UI documentation will be available at: `http://localhost:8080/swagger-ui.html`

---

## 📑 License

Distributed under the MIT License. See [`LICENSE`](LICENSE) for more information.
