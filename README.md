# MindFlow

MindFlow is a local-first AI knowledge management project. This workspace is currently configured for local development and testing only.

Current status: Phase 1 local skeleton is runnable. Phase 2 is planned in [mindflow/docs/phase-2-roadmap.md](mindflow/docs/phase-2-roadmap.md) and focuses on Markdown chunking, local keyword retrieval, then embedding and pgvector search.

## Local Development Stack

| Part | Local Address |
| --- | --- |
| Backend API | `http://localhost:8080` |
| Frontend | `http://localhost:5173` |
| PostgreSQL | `localhost:5432` |

Local database:

```text
database: mindflow
username: mindflow
password: mindflow
jdbc url: jdbc:postgresql://localhost:5432/mindflow
```

The backend config is in:

```text
mindflow/backend/mindflow-api/src/main/resources/application.yml
```

## Backend

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml test
mvn --settings .mvn/settings.xml -pl mindflow-api -am spring-boot:run
```

The API module explicitly includes PostgreSQL, Flyway, and Spring Data JPA dependencies for local database testing.

## Frontend

```bash
cd mindflow/frontend
npm ci
npm run dev
```

The Vite dev server proxies `/api` to `http://localhost:8080`.

## Database Check

```bash
psql -h localhost -U mindflow -d mindflow -c "select extname, extversion from pg_extension where extname='vector';"
```

Expected tables:

```text
users
workspace
knowledge_object
knowledge_relation
knowledge_source
ai_task
```

Project README: [mindflow/README.md](mindflow/README.md)
