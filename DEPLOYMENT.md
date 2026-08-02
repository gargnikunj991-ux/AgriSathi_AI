# 🚀 AgriSathi AI — Deployment & Infrastructure Guide

**Version:** 1.0  
**Target Environments:** Local Dev, Staging, Cloud Production (AWS, Render, Railway, DigitalOcean)  

---

## 1. Environment Configuration

The application uses environment variables injected at runtime or loaded from `.env` files via Spring Boot placeholder resolution.

### Production Environment Variables Checklist

| Variable Name | Description | Example Value |
| :--- | :--- | :--- |
| `PORT` | HTTP Server Listener Port | `8080` |
| `DB_URL` | PostgreSQL Connection String | `jdbc:postgresql://postgres.example.com:5432/agrisathi_prod` |
| `DB_USERNAME` | Production DB Username | `agrisathi_admin` |
| `DB_PASSWORD` | Secure DB Password | `SuperSecretDbPassword123!` |
| `DB_POOL_MAX` | Hikari Connection Pool Max | `20` |
| `DDL_AUTO` | Hibernate Schema Strategy | `validate` (Use `validate` in production!) |
| `JWT_SECRET` | 64-char Hex Secret Key | `8f94d...a3e` |
| `JWT_EXPIRATION_MS` | Token Expiry in Milliseconds | `86400000` (24 Hours) |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary Storage Name | `agrisathi-cloud` |
| `CLOUDINARY_API_KEY` | Cloudinary API Key | `123456789012345` |
| `CLOUDINARY_API_SECRET` | Cloudinary Secret | `abcde_123456789` |
| `CORS_ALLOWED_ORIGINS` | Permitted Frontend Origins | `https://agrisathi.app,https://www.agrisathi.app` |

---

## 2. Docker Container Deployment

### 2.1. Backend Dockerfile

Save the following as `Dockerfile` inside `agrisathi-backend/`:

```dockerfile
# Stage 1: Build JAR using Maven & Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Minimal Execution Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/target/agrisathi-backend-1.0.0.jar app.jar

# Run Application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
```

### 2.2. Docker Compose Infrastructure Setup (`docker-compose.yml`)

Create `docker-compose.yml` at project root:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    container_name: agrisathi-postgres
    restart: always
    environment:
      POSTGRES_DB: agrisathidb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgrespassword
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  backend:
    build:
      context: ./agrisathi-backend
      dockerfile: Dockerfile
    container_name: agrisathi-backend
    restart: always
    ports:
      - "8080:8080"
    environment:
      - PORT=8080
      - DB_URL=jdbc:postgresql://postgres:5432/agrisathidb
      - DB_USERNAME=postgres
      - DB_PASSWORD=postgrespassword
      - JWT_SECRET=c3VwZXJzZWNyZXRqd3RrZXlhZ3Jpc2F0aGlhaXN1cGVyc2VjcmV0a2V5MTIzNDU2
      - CORS_ALLOWED_ORIGINS=http://localhost:3000
    depends_on:
      - postgres

volumes:
  pgdata:
```

### Run Docker Compose:
```bash
docker-compose up -d --build
```

---

## 3. Manual Server Deployment (Linux Systemd)

### 1. Build Executable JAR
```bash
cd agrisathi-backend
mvn clean package -DskipTests
```
The output file is generated at `target/agrisathi-backend-1.0.0.jar`.

### 2. Create Systemd Service File (`/etc/systemd/system/agrisathi.service`)
```ini
[Unit]
Description=AgriSathi AI Backend Spring Boot Service
After=network.target postgresql.service

[Service]
User=ubuntu
WorkingDirectory=/opt/agrisathi
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod /opt/agrisathi/agrisathi-backend-1.0.0.jar
SuccessExitStatus=143
Restart=always
RestartSec=10
EnvironmentFile=/opt/agrisathi/.env

[Install]
WantedBy=multi-user.target
```

### 3. Start & Enable Service
```bash
sudo systemctl daemon-reload
sudo systemctl start agrisathi
sudo systemctl enable agrisathi
```

---

## 4. NGINX Reverse Proxy & SSL Configuration

Example NGINX config block (`/etc/nginx/sites-available/agrisathi`):

```nginx
server {
    listen 80;
    server_name api.agrisathi.app;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```
Enable SSL via Certbot:
```bash
sudo certbot --nginx -d api.agrisathi.app
```
