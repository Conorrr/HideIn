# HideIn

Inject hidden (invisible) text into PDFs for bypassing LLM-based ATS systems.

## Stack

- **Frontend**: SvelteKit 5 + Tailwind v4 + TypeScript (static adapter)
- **Backend**: Java 21 + Javalin + PDFBox

## Commands

### Frontend (`frontend/`)
```bash
pnpm install
pnpm dev          # dev server
pnpm build        # static build
pnpm check        # typecheck
```

### Backend (`backend/`)
```bash
./gradlew run           # dev server
./gradlew shadowJar     # fat jar
```

## API

### `POST /api/inject`
Multipart form: `file` (PDF), `text` (string to inject). Returns modified PDF.
- 400: missing/invalid PDF or empty text
- 413: file exceeds limit (10 MB)

### `GET /health`
Health check endpoint.

## Architecture

- Hidden text injected as tiny white text at bottom of first page
- No storage — streams in memory only
- Frontend: file picker + textarea + download button

## Environment

Frontend requires `PUBLIC_API_URL` (see `frontend/.env.example`).

## CI/CD

- Backend: Docker image pushed to GHCR on push to main
- Frontend: Vercel auto-deploy via Git integration
