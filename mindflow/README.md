# MindFlow

MindFlow is a local-first AI knowledge management project. The current development focus is local backend and frontend testing with a local PostgreSQL database.

Current status: Phase 1 local skeleton is runnable. The next work is Phase 2: Markdown chunking, local keyword retrieval, then embedding and pgvector search. See [docs/phase-2-roadmap.md](docs/phase-2-roadmap.md).

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
├── README.md
├── CONTRIBUTING.md
└── LICENSE
```

## Local Database

The backend is configured to use the local PostgreSQL database by default:

```text
jdbc url: jdbc:postgresql://localhost:5432/mindflow
database: mindflow
username: mindflow
password: mindflow
```

pgvector is required and should be installed in the `mindflow` database:

```sql
CREATE EXTENSION IF NOT EXISTS vector;
```

Database migrations live in:

```text
backend/mindflow-infrastructure/src/main/resources/db/migration
```

The local connection defaults are in:

```text
backend/mindflow-api/src/main/resources/application.yml
```

## Backend Local Run

Run tests:

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml test
```

Start the API:

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml -pl mindflow-api -am spring-boot:run
```

The API listens on:

```text
http://localhost:8080
```

## Frontend Local Run

Install dependencies:

```bash
cd mindflow/frontend
npm ci
```

Start the dev server:

```bash
cd mindflow/frontend
npm run dev
```

The frontend listens on:

```text
http://localhost:5173
```

Vite proxies `/api` requests to `http://localhost:8080`.

## Implemented Scope

- Spring Boot multi-module backend.
- PostgreSQL, Flyway, JPA, and pgvector local database setup.
- Knowledge creation API: `POST /api/v1/knowledge`.
- RAG chat API: `POST /api/v1/chat`.
- React + TypeScript + Vite frontend.
- Frontend Chinese text has been repaired, and Markdown preview now preserves paragraph line breaks.

## Not Implemented Yet

- Markdown is not yet persisted as searchable chunks.
- Keyword retrieval over `knowledge_chunk` is not implemented yet.
- Embedding generation and pgvector similarity search are not wired into the retrieval path yet.
- `/chat` does not yet generate answers from real retrieved knowledge context.

See [docs/deployment.md](docs/deployment.md) for the local development checklist.
