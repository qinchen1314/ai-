# Contributing

MindFlow development follows an issue-by-issue workflow.

## Before Making Changes

- Read the current issue and existing architecture.
- Keep changes scoped to the requested module.
- Do not modify unrelated files or modules.
- Add tests when behavior or contracts are introduced.
- Explain what changed, why it changed, impact, and verification.

## Core Architectural Rules

- Domain code should not depend on Spring.
- Application code owns orchestration and depends on domain ports.
- Infrastructure implements external persistence and framework adapters.
- AI module wraps model/provider-specific behavior behind interfaces.
- API module translates HTTP requests into application commands and results.
- AI processing should be designed as asynchronous work.
- AI-generated answers must support source tracking.
- MVP features should stay simple and extensible.

## Verification

Run the smallest relevant check first, then the full check before committing when the change touches shared contracts.

Backend:

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml test
```

Frontend:

```bash
cd mindflow/frontend
npm ci
npm audit --audit-level=moderate
npm run build
```

Docker-related changes should be checked with:

```bash
cd mindflow
docker compose config
docker compose up --build
```

If Docker is not available locally, document that limitation in the handoff.

## Commit Style

Prefer one commit per issue. Keep commit messages short and action-oriented, for example:

```text
Add RAG chat API
```
