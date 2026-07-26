# MindFlow

MindFlow is an AI-native personal knowledge management system.

The v0.1 goal is to build the first usable knowledge engine:

- Create and store Markdown-based knowledge.
- Let AI understand knowledge through summaries and embeddings.
- Retrieve personal knowledge with vector and keyword search.
- Answer questions with source tracking.

## Project Structure

```text
mindflow/
├── backend/
│   ├── mindflow-domain/
│   ├── mindflow-application/
│   ├── mindflow-infrastructure/
│   ├── mindflow-ai/
│   └── mindflow-api/
├── frontend/
├── docs/
├── docker-compose.yml
├── README.md
├── CONTRIBUTING.md
└── LICENSE
```

## Implemented Scope

- Backend monorepo with Spring Boot, DDD-style domain, application ports, infrastructure adapters, and API layer.
- Knowledge object and knowledge relation domain models.
- PostgreSQL/Flyway schema including pgvector-ready embeddings and asynchronous AI task table.
- Knowledge creation API: `POST /api/v1/knowledge`.
- Markdown parsing application service.
- LLM and embedding abstractions.
- AI task workflow with embedding worker.
- Vector search and hybrid search application contracts.
- RAG chat API: `POST /api/v1/chat`.
- React + TypeScript + Vite frontend with `/editor`, `/chat`, and `/knowledge` routes.
- Docker Compose stack for backend, frontend, PostgreSQL, and Redis.
- GitHub Actions CI for backend tests and frontend audit/build.

Some integrations are intentionally abstract in v0.1 scaffolding: real LLM HTTP calls, real pgvector search adapters, and production-grade async workers are represented by ports and tested services, ready for the next implementation pass.

## Local Development

### Backend

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml test
```

### Frontend

```bash
cd mindflow/frontend
npm ci
npm run build
```

## Development Principle

MindFlow is built one issue at a time. Each change should stay scoped, preserve the existing architecture, include relevant verification, and avoid unrelated dependencies.

## Docker

Run the full local stack from the `mindflow/` directory:

```bash
docker compose up --build
```

This starts the Spring Boot backend, Vite-built frontend served by Nginx, PostgreSQL with pgvector, and Redis.

More details:

- Architecture: [`docs/architecture.md`](docs/architecture.md)
- Deployment: [`docs/deployment.md`](docs/deployment.md)
- Contribution workflow: [`CONTRIBUTING.md`](CONTRIBUTING.md)
