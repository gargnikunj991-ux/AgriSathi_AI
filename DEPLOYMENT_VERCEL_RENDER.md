# AgriSathi Deployment Guide - Vercel & Render

## Part 1: Frontend Deployment to Vercel

### Step 1: Prepare Your GitHub Repository
1. Push all code to GitHub (frontend, backend, and docs on respective branches)
2. Ensure your `frontend` branch is up-to-date
3. Make sure `.gitignore` excludes: `node_modules/`, `.env.local`, `.next/`

### Step 2: Set Up Vercel Account
1. Go to [vercel.com](https://vercel.com)
2. Sign up/Login with GitHub account
3. Click "Add New → Project"

### Step 3: Import Frontend Project
1. Select your `AgriSathi_AI` repository
2. Under "Root Directory", set to `frontend/`
3. Click "Deploy"

### Step 4: Configure Environment Variables
After initial deployment, go to **Settings → Environment Variables** and add:

```
NEXT_PUBLIC_API_URL=https://your-render-backend-url.onrender.com/api
```

**Note:** Replace `your-render-backend-url` with your actual Render backend URL (you'll get this after deploying backend)

### Vercel Automatic Deployments
- **Branch Deployments:** Every push to `main` auto-deploys
- **Preview Deployments:** Pull requests get preview URLs
- **Rollback:** Easy rollback to previous versions in Vercel Dashboard

---

## Part 2: Backend Deployment to Render

### Step 1: Prepare Render Configuration
✅ Already created: `render.yaml` in `agrisathi-backend/`

### Step 2: Set Up Render Account
1. Go to [render.com](https://render.com)
2. Sign up/Login with GitHub account

### Step 3: Deploy Backend Service
1. Click "New +" → "Web Service"
2. Select your GitHub repository
3. Configure:
   - **Name:** `agrisathi-backend` (or your choice)
   - **Root Directory:** `agrisathi-backend`
   - **Runtime:** `Docker`
   - **Build Command:** (Leave empty - Docker handles it)
   - **Start Command:** (Leave empty - Docker handles it)

### Step 4: Add Environment Variables
In **Environment** section, add these (get values from your setup):

```
PORT=8080
DB_URL=postgresql://user:password@your-db-host:5432/agrisathi_prod
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password
DB_POOL_MAX=20
DDL_AUTO=validate
JWT_SECRET=your-64-char-hex-secret-key
JWT_EXPIRATION_MS=86400000
CLOUDINARY_CLOUD_NAME=your-cloudinary-name
CLOUDINARY_API_KEY=your-cloudinary-key
CLOUDINARY_API_SECRET=your-cloudinary-secret
CORS_ALLOWED_ORIGINS=https://your-app.vercel.app,https://www.your-app.vercel.app
```

### Step 5: Deploy
1. Click "Create Web Service"
2. Render will automatically build and deploy from Dockerfile
3. After successful deployment, you'll get a URL like: `https://agrisathi-backend.onrender.com`

### Step 6: Update Frontend with Backend URL
Go back to Vercel:
1. **Settings → Environment Variables**
2. Update `NEXT_PUBLIC_API_URL` with your Render backend URL:
   ```
   NEXT_PUBLIC_API_URL=https://agrisathi-backend.onrender.com/api
   ```
3. Trigger a redeployment by pushing code or clicking "Redeploy"

---

## Part 3: Database Setup (PostgreSQL)

### Option A: Render Managed PostgreSQL
1. In Render dashboard, click "New +" → "PostgreSQL"
2. Configure database name, region, instance type
3. Copy connection string
4. Add to your Backend environment variables

### Option B: External Database (AWS RDS, Heroku Postgres, etc.)
1. Provide connection string in `DB_URL` environment variable
2. Ensure Render IP is whitelisted in database security groups

---

## Part 4: Testing Deployment

### Test Frontend
```bash
# Visit your Vercel URL
https://your-project.vercel.app
```

### Test Backend
```bash
# Test health endpoint
curl https://your-backend.onrender.com/api/health

# Test with API endpoint
curl -X GET https://your-backend.onrender.com/api/crops
```

### Test API Connection
1. Open your frontend in browser
2. Check browser DevTools (F12 → Network tab)
3. Verify API requests go to correct backend URL
4. Check for CORS errors in console

---

## Part 5: Important Considerations

### Cold Starts
- **Vercel:** Function cold starts ~50-100ms (negligible)
- **Render:** First request after inactivity takes 30-60s (free tier)
  - **Solution:** Use Render's paid plan for instant availability, or use render-keep-alive

### Scaling
- **Vercel:** Auto-scales based on traffic (serverless)
- **Render:** Manual scaling or auto-scaling policies available

### Monitoring & Logs
- **Vercel:** Dashboard → Logs
- **Render:** Dashboard → Logs

### Security
- ✅ Enable HTTPS (both platforms do this automatically)
- ✅ Use environment variables for secrets
- ✅ Configure CORS properly
- ✅ Use strong JWT secrets

---

## Part 6: CI/CD Workflow

### Automatic Deployments
Both Vercel and Render support automatic deployments on GitHub push:

1. **Frontend:** Vercel auto-deploys on push to branch
2. **Backend:** Render auto-deploys on `agrisathi-backend/` changes

### Manual Deployments (if needed)
- **Vercel:** Dashboard → Deployments → Redeploy
- **Render:** Dashboard → Services → Manual Deploy

---

## Troubleshooting

### Frontend can't reach Backend API
1. Check `NEXT_PUBLIC_API_URL` environment variable in Vercel
2. Verify CORS_ALLOWED_ORIGINS in backend matches Vercel domain
3. Test backend directly: `curl https://backend-url/api/health`

### Backend service won't start
1. Check Render Logs for errors
2. Verify all environment variables are set
3. Ensure database is accessible
4. Verify Dockerfile is valid

### Database connection fails
1. Check DB_URL format: `postgresql://user:pass@host:port/dbname`
2. Verify database is running
3. Check firewall/security groups allow connection
4. Verify credentials are correct

---

## Next Steps
1. ✅ Push `vercel.json` and `render.yaml` to main branch
2. ✅ Connect Vercel to GitHub
3. ✅ Connect Render to GitHub
4. ✅ Set environment variables
5. ✅ Test deployment
6. ✅ Monitor logs for issues
