# MindFlow Deployment

MindFlow can be started locally with Docker Compose from the `mindflow/` directory.

```bash
docker compose up --build
```

## Services

| Service | Port | Purpose |
| --- | ---: | --- |
| `frontend` | `5173` | Nginx serving the built React app |
| `backend` | `8080` | Spring Boot API |
| `postgres` | `5432` | PostgreSQL with pgvector |
| external Redis | `81.70.47.98:6379` | Redis for future async/caching work |

## Compose Files

The main stack is defined in:

```text
mindflow/docker-compose.yml
```

The older database-only compose file remains in:

```text
mindflow/docker/docker-compose.yml
```

Use the root compose file for full-stack local deployment.

## Backend Image

The backend image is built from:

```text
mindflow/backend/Dockerfile
```

It packages the Spring Boot API module:

```bash
mvn -pl mindflow-api -am package -DskipTests
```

Runtime environment variables used by compose:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_FLYWAY_ENABLED`
- `MINDFLOW_PERSISTENCE_ENABLED`
- `SPRING_DATA_REDIS_HOST=81.70.47.98`
- `SPRING_DATA_REDIS_PORT=6379`

## Frontend Image

The frontend image is built from:

```text
mindflow/frontend/Dockerfile
```

It runs:

```bash
npm ci
npm run build
```

The resulting static files are served by Nginx. Requests under `/api/` are proxied to the backend service.

## Local Verification

Without Docker:

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml test

cd ../frontend
npm ci
npm audit --audit-level=moderate
npm run build
```

With Docker installed:

```bash
cd mindflow
docker compose config
docker compose up --build
```
