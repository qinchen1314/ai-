# MindFlow Phase 2 Roadmap

Phase 1 has produced a local runnable skeleton: Spring Boot modules, local PostgreSQL with pgvector, Flyway migrations, a basic `KnowledgeObject` persistence flow, and React pages for editing and chat.

Phase 2 turns that skeleton into a small but real local knowledge retrieval loop.

## Goal

Build a local-first Markdown knowledge loop:

```text
Markdown input
  -> document save
  -> chunk parsing
  -> keyword retrieval
  -> chat answer from retrieved context
  -> source display
```

Embedding and pgvector come after the keyword retrieval loop is working.

## Priorities

| Priority | Work | Importance | Why |
| --- | --- | --- | --- |
| P0 | Fix frontend Chinese text and Markdown preview line handling | Critical | The UI must be readable before testing knowledge workflows. |
| P0 | Add `knowledge_chunk` schema and model | Critical | Retrieval should target chunks, not whole Markdown documents. |
| P0 | Parse Markdown into heading, paragraph, list, and code chunks on save | Critical | This creates the unit of search, source citation, and later embedding. |
| P1 | Implement local keyword search over chunks | High | It gives an immediately testable retrieval path without waiting for LLM or embedding. |
| P1 | Make `/chat` answer from retrieved chunks | High | This closes the first useful RAG-like loop, even with a deterministic local answer. |
| P2 | Add embedding generation task processing | High | Semantic retrieval depends on embedding, but it should not block the keyword MVP. |
| P2 | Store chunk embeddings in pgvector and add vector search | High | This upgrades retrieval quality after chunking and keyword search are stable. |
| P3 | Add hybrid search ranking | Medium | Merge keyword and vector results once both paths exist. |
| P3 | Connect a real LLM provider for final answer synthesis | Medium | Valuable, but only after the retrieved context and sources are reliable. |

## Proposed Work Breakdown

### 1. Frontend Repair

Status: started in Phase 1 cleanup.

Acceptance criteria:

- All visible Chinese text renders correctly.
- Markdown preview preserves paragraph line breaks.
- Code blocks preserve original line breaks.
- `/editor`, `/chat`, and `/knowledge` routes build without TypeScript errors.

### 2. Knowledge Chunk Storage

Add a new table:

```sql
CREATE TABLE knowledge_chunk (
    id UUID PRIMARY KEY,
    object_id UUID NOT NULL REFERENCES knowledge_object(id) ON DELETE CASCADE,
    chunk_index INTEGER NOT NULL,
    type VARCHAR(40) NOT NULL,
    heading TEXT,
    content TEXT NOT NULL,
    start_line INTEGER NOT NULL,
    end_line INTEGER NOT NULL,
    embedding vector(1536),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_knowledge_chunk_object_index UNIQUE (object_id, chunk_index)
);

CREATE INDEX idx_knowledge_chunk_object_id ON knowledge_chunk(object_id);
CREATE INDEX idx_knowledge_chunk_type ON knowledge_chunk(type);
CREATE INDEX idx_knowledge_chunk_content_fts
    ON knowledge_chunk USING GIN (to_tsvector('simple', content));
```

Chunk types:

```text
HEADING
PARAGRAPH
LIST
CODE
```

### 3. Markdown Chunk Parser

The backend parser should produce chunks with:

- `chunkIndex`
- `type`
- `heading`
- `content`
- `startLine`
- `endLine`

Rules:

- Headings become `HEADING` chunks and update current heading context.
- Consecutive paragraph lines become one `PARAGRAPH` chunk, preserving line breaks.
- Consecutive `- ` lines become one `LIST` chunk.
- Fenced code blocks become one `CODE` chunk.

### 4. Save Markdown and Chunks Together

When `POST /api/v1/knowledge` receives Markdown content:

- Save the parent `KnowledgeObject`.
- Parse Markdown chunks.
- Replace chunks for that object in one transaction.
- Create embedding tasks later at chunk level, not document level.

### 5. Keyword Search First

Add a chunk keyword repository:

```text
searchByText(query, limit) -> List<KnowledgeSnippet>
```

Initial ranking can be simple:

- PostgreSQL full-text match first.
- Fallback to `ILIKE` for Chinese/local tests.
- Return source fields: object id, title, chunk id, chunk index, start/end line.

### 6. `/chat` From Search Results

Before connecting a real LLM, make `/chat` return a deterministic local answer:

```text
我找到了以下相关内容：
1. ...
2. ...
```

Sources should include document title and line range.

This makes the retrieval loop testable without API keys.

### 7. Embedding and pgvector

After keyword search works:

- Generate embeddings per chunk.
- Store embeddings on `knowledge_chunk.embedding`.
- Implement vector similarity search.
- Merge vector and keyword results in hybrid search.

### 8. Real LLM Answering

Only after source retrieval is stable:

- Build prompt from retrieved chunks.
- Call configured LLM provider.
- Return answer plus sources.
- Keep deterministic fallback when no LLM key is configured.

## Definition of Done for Phase 2

- A Markdown note can be saved through the backend.
- The note is split into chunks with line ranges.
- A keyword query returns matching chunks.
- `/chat` returns an answer assembled from local search results.
- The frontend shows readable Chinese text and preserves Markdown line breaks.
- README explains the actual state without overstating AI capabilities.
