# Deploy AgriSathi AI on Render and Vercel

## 1. Push to GitHub

This repository already points to:

```bash
https://github.com/gargnikunj991-ux/AgriSathi_AI.git
```

Commit the deployment changes, push a branch, and open a pull request:

```bash
git add .
git commit -m "Add Render and Vercel deployment setup"
git switch -c deploy/render-vercel-setup
git push -u origin deploy/render-vercel-setup
```

This repository protects `main`, so direct pushes to `main` are rejected. Open the pull request from:

```text
https://github.com/gargnikunj991-ux/AgriSathi_AI/pull/new/deploy/render-vercel-setup
```

## 2. Deploy Backend on Render

Use the root-level `render.yaml` as a Render Blueprint.

1. Open Render Dashboard.
2. Create a new Blueprint.
3. Connect this GitHub repository.
4. Select branch `main`.
5. Keep Blueprint Path as `render.yaml`.
6. Fill the prompted secret values:
   - `CLOUDINARY_CLOUD_NAME`
   - `CLOUDINARY_API_KEY`
   - `CLOUDINARY_API_SECRET`
   - `CORS_ALLOWED_ORIGINS`

For `CORS_ALLOWED_ORIGINS`, use your Vercel frontend URL after Vercel creates it, for example:

```text
https://your-vercel-project.vercel.app
```

Render creates:

- Docker web service: `agrisathi-backend`
- PostgreSQL database: `agrisathi-postgres`
- Health check: `/actuator/health`

After deployment, your backend base URL will be:

```text
https://agrisathi-backend.onrender.com/api/v1
```

If Render assigns a different service hostname, use that hostname instead.

## 3. Deploy Frontend on Vercel

Create a Vercel project from the same GitHub repository.

Use these project settings:

```text
Framework Preset: Next.js
Root Directory: frontend
Install Command: npm ci
Build Command: npm run build
Output Directory: .next
```

Add these Vercel environment variables:

```text
NEXT_PUBLIC_API_BASE_URL=https://agrisathi-backend.onrender.com/api/v1
NEXT_PUBLIC_USE_MOCK=false
```

Redeploy the Vercel project after adding environment variables.

## 4. Production Notes

The included Render Blueprint uses free instances to avoid surprise costs. Render free Postgres is best for demos and testing, not durable production data. Upgrade the backend and database plans before relying on it for real users.
