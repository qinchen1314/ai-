CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE workspace (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(120) NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_workspace_user_id ON workspace(user_id);

CREATE TABLE knowledge_object (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL REFERENCES workspace(id),
    type VARCHAR(40) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    summary TEXT,
    status VARCHAR(40) NOT NULL,
    importance DOUBLE PRECISION NOT NULL DEFAULT 0,
    embedding vector(1536),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_knowledge_object_importance CHECK (importance >= 0 AND importance <= 1)
);

CREATE INDEX idx_knowledge_object_workspace_id ON knowledge_object(workspace_id);
CREATE INDEX idx_knowledge_object_type ON knowledge_object(type);
CREATE INDEX idx_knowledge_object_status ON knowledge_object(status);

CREATE TABLE knowledge_relation (
    id UUID PRIMARY KEY,
    source_id UUID NOT NULL REFERENCES knowledge_object(id),
    target_id UUID NOT NULL REFERENCES knowledge_object(id),
    relation_type VARCHAR(40) NOT NULL,
    confidence DOUBLE PRECISION NOT NULL DEFAULT 1,
    created_by UUID REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_knowledge_relation_confidence CHECK (confidence >= 0 AND confidence <= 1),
    CONSTRAINT ck_knowledge_relation_distinct_objects CHECK (source_id <> target_id)
);

CREATE INDEX idx_knowledge_relation_source_id ON knowledge_relation(source_id);
CREATE INDEX idx_knowledge_relation_target_id ON knowledge_relation(target_id);
CREATE INDEX idx_knowledge_relation_type ON knowledge_relation(relation_type);

CREATE TABLE knowledge_source (
    id UUID PRIMARY KEY,
    object_id UUID NOT NULL REFERENCES knowledge_object(id),
    source_type VARCHAR(40) NOT NULL,
    source_url TEXT,
    file_path TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_knowledge_source_object_id ON knowledge_source(object_id);
CREATE INDEX idx_knowledge_source_type ON knowledge_source(source_type);

CREATE TABLE ai_task (
    id UUID PRIMARY KEY,
    type VARCHAR(60) NOT NULL,
    status VARCHAR(40) NOT NULL,
    input JSONB NOT NULL,
    output JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_ai_task_status ON ai_task(status);
CREATE INDEX idx_ai_task_type ON ai_task(type);
