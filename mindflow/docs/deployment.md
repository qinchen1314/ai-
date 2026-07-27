# MindFlow Local Development

This project is currently configured for local development only.

## Required Local Services

Use the PostgreSQL service installed on this machine:

| Service | Address | Notes |
| --- | --- | --- |
| PostgreSQL 17 | `localhost:5432` | Database `mindflow` |
| pgvector | PostgreSQL extension | Installed in the `mindflow` database |
| Backend API | `localhost:8080` | Spring Boot local process |
| Frontend dev server | `localhost:5173` | Vite local process |

Database credentials for local testing:

```text
database: mindflow
username: mindflow
password: mindflow
jdbc url: jdbc:postgresql://localhost:5432/mindflow
```

The backend reads these defaults from:

```text
mindflow/backend/mindflow-api/src/main/resources/application.yml
```

Environment variables can still override them when needed:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
SPRING_FLYWAY_ENABLED
MINDFLOW_PERSISTENCE_ENABLED
```

## Backend

Run tests:

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml test
```

Start the API locally:

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml -pl mindflow-api -am spring-boot:run
```

Flyway is enabled by default. It uses the SQL migrations under:

```text
mindflow/backend/mindflow-infrastructure/src/main/resources/db/migration
```

## Frontend

Install dependencies:

```bash
cd mindflow/frontend
npm ci
```

Start the Vite dev server:

```bash
cd mindflow/frontend
npm run dev
```

The Vite dev server proxies `/api` to:

```text
http://localhost:8080
```

## Local Verification

Confirm pgvector:

```bash
psql -h localhost -U mindflow -d mindflow -c "select extname, extversion from pg_extension where extname='vector';"
```

Confirm tables:

```bash
psql -h localhost -U mindflow -d mindflow -c "select table_name from information_schema.tables where table_schema='public' order by table_name;"
```
