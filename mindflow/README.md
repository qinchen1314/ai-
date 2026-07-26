# MindFlow

MindFlow 是一个 AI 原生个人知识管理系统。

> 仓库首页 README 位于项目根目录：[`../README.md`](../README.md)

v0.1 目标是构建第一个可用的 AI 知识引擎：

- 创建和存储 Markdown 知识。
- 通过摘要和 Embedding 让 AI 理解知识。
- 使用向量搜索和关键词搜索检索个人知识。
- 通过 RAG Chat 回答问题并返回来源。

## 项目结构

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

## 已实现范围

- Spring Boot 后端多模块工程。
- 知识对象、知识关系、AI 任务、搜索模型。
- PostgreSQL/Flyway/pgvector 初始化。
- 知识创建接口：`POST /api/v1/knowledge`。
- RAG Chat 接口：`POST /api/v1/chat`。
- React + TypeScript + Vite 前端。
- Docker Compose 一键启动。
- GitHub Actions CI。

真实 LLM HTTP 调用、真实 pgvector 检索适配器、生产级异步任务 Worker 仍是后续实现项。

## 本地开发

### 后端

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml test
```

### 前端

```bash
cd mindflow/frontend
npm ci
npm run build
```

## 开发原则

MindFlow 按 issue 逐步构建。每次变更都应该保持范围清晰、遵守现有架构、包含必要验证，并避免无关依赖。

## Docker

从 `mindflow/` 目录启动完整本地栈：

```bash
docker compose up --build
```

这会启动 Spring Boot 后端、Nginx 托管的前端、PostgreSQL/pgvector 和 Redis。

更多文档：

- 架构说明：[`docs/architecture.md`](docs/architecture.md)
- 部署说明：[`docs/deployment.md`](docs/deployment.md)
- 贡献规范：[`CONTRIBUTING.md`](CONTRIBUTING.md)
