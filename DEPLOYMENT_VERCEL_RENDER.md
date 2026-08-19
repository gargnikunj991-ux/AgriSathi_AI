# 🚀 Render & Vercel Deployment Guide

## 1. Deploying Backend to Render

### Option A: Automatic via Render Blueprint (`render.yaml`)
1. In your Render Dashboard, click **New +** -> **Blueprint**.
2. Select your repository (`gargnikunj991-ux/AgriSathi_AI`).
3. Render will detect the root [render.yaml](file:///D:/augest%20hackathon/render.yaml) automatically.
4. Fill in the required environment variables:
   - `DB_URL`: Your PostgreSQL JDBC URL (e.g. from Render PostgreSQL, Supabase, Neon, or Railway: `jdbc:postgresql://<host>:5432/<dbname>`)
   - `DB_USERNAME`: Database username
   - `DB_PASSWORD`: Database password
   - `JWT_SECRET`: 64-character secret key
   - `CORS_ALLOWED_ORIGINS`: Your Vercel frontend domain URL (e.g. `https://agrisathi-frontend.vercel.app`)
5. Click **Apply**.

---

### Option B: Manual Web Service Deployment (Render UI)
If creating a Web Service directly without Blueprints:
1. Click **New +** -> **Web Service**.
2. Connect your Git repository.
3. Configure the following fields in the creation form:
   - **Name**: `agrisathi-backend`
   - **Language / Runtime**: `Docker`
   - **Root Directory**: `agrisathi-backend` *(or leave blank since a root Dockerfile is also provided)*
   - **Dockerfile Path**: `agrisathi-backend/Dockerfile` *(or `Dockerfile` if Root Directory is set to `agrisathi-backend`)*
   - **Docker Build Context**: `agrisathi-backend`
   - **Instance Type**: Free or Starter
4. Under **Environment Variables**, add:
   - `PORT`: `8080`
   - `DB_URL`: `jdbc:postgresql://<host>:5432/<dbname>`
   - `DB_USERNAME`: `<username>`
   - `DB_PASSWORD`: `<password>`
   - `JWT_SECRET`: `<your_64_char_jwt_secret>`
   - `CORS_ALLOWED_ORIGINS`: `https://your-frontend.vercel.app`
   - `DDL_AUTO`: `update`
   - `DB_POOL_MAX`: `5`
   - `DB_POOL_MIN`: `1`
5. Under **Health Check Path**, set:
   - `/api/v1/health`
6. Click **Create Web Service**.

---

## 2. Deploying Frontend to Vercel

1. Push your frontend project to GitHub.
2. In Vercel Dashboard, click **Add New...** -> **Project**.
3. Import the repository.
4. Set the Framework Preset (e.g. Next.js, Vite, React).
5. Add Environment Variable:
   - `NEXT_PUBLIC_API_URL` or `VITE_API_URL`: `https://<your-render-backend-url>.onrender.com`
6. Click **Deploy**.
