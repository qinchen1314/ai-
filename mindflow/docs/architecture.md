# MindFlow Architecture

MindFlow is an AI-native personal knowledge management system. The v0.1 architecture favors small, deep modules with clear contracts over broad feature coupling.

## Backend Modules

```text
backend/
├── mindflow-domain
├── mindflow-application
├── mindflow-infrastructure
├── mindflow-ai
└── mindflow-api
```

### mindflow-domain

Pure Java domain model. It does not depend on Spring.

Current domain areas:

- `knowledge`: `KnowledgeObject`, `KnowledgeType`, `KnowledgeStatus`, `KnowledgeRepository`
- `relation`: `KnowledgeRelation`, `RelationType`
- `task`: `AiTask`, `TaskType`, `TaskStatus`, `AiTaskRepository`
- `search`: `KnowledgeSnippet`, `VectorSearchRepository`, `FullTextSearchRepository`

### mindflow-application

Application orchestration and use case contracts.

Current use cases:

- Create knowledge.
- Parse Markdown into a knowledge creation command.
- Vector search from a natural-language question.
- Hybrid search by combining vector search and full-text search.
- RAG chat: question → search → context → LLM → answer with sources.

### mindflow-infrastructure

Framework and persistence adapters.

Current scope:

- Flyway migration for PostgreSQL and pgvector.
- JPA mapping for `knowledge_object`.
- Repository adapter for knowledge objects.

### mindflow-ai

AI provider abstractions and workers.

Current scope:

- `LLMProvider` and provider shells for OpenAI/DeepSeek.
- `EmbeddingService` and embedding client abstraction.
- `EmbeddingTaskWorker` for AI task processing.
- Gateway adapters that connect application ports to AI services.

### mindflow-api

HTTP boundary built with Spring Boot.

Current endpoints:

- `POST /api/v1/knowledge`
- `POST /api/v1/chat`

## Frontend

The frontend is React + TypeScript + Vite.

Current routes:

- `/editor`: Markdown editing, local saving, and safe preview.
- `/chat`: ChatGPT-like RAG chat UI backed by `POST /api/v1/chat`.
- `/knowledge`: placeholder route for knowledge browsing.

## Data Storage

PostgreSQL stores users, workspaces, knowledge objects, knowledge relations, knowledge sources, and AI tasks.

pgvector is enabled and `knowledge_object.embedding` is ready for semantic search.

Redis uses the external instance `81.70.47.98:6379` for future async processing and caching.

## Key Flow

```text
Markdown input
  -> Markdown parser
  -> Knowledge creation use case
  -> Knowledge repository
  -> AI task: EMBEDDING/PENDING
  -> Embedding worker
  -> Vector/hybrid search
  -> RAG chat answer with sources
```

## Boundaries and Intentional Gaps

The project currently defines several ports before their production adapters:

- Real LLM HTTP calls.
- Real embedding HTTP calls.
- Real pgvector and PostgreSQL full-text search adapters.
- Production async task polling/queueing.

This is intentional for v0.1: contracts are in place, tests cover behavior, and each missing adapter can be implemented as a focused follow-up issue.
