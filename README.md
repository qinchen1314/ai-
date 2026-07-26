# MindFlow

MindFlow 是一个 AI 原生个人知识管理系统，目标是把 Markdown 笔记、知识检索、Embedding、RAG 问答和来源引用串成一个可持续扩展的知识大脑。

当前版本是 v0.1 工程骨架，已经完成后端分层、前端基础页面、Docker 部署、CI/CD 和项目文档。

## 已实现内容

- Spring Boot 后端多模块工程。
- DDD 风格领域层：知识对象、知识关系、AI 任务、搜索结果模型。
- PostgreSQL + Flyway 初始化脚本，已启用 pgvector。
- Redis 使用外部实例：`81.70.47.98:6379`，用于后续异步任务和缓存。
- 知识创建接口：`POST /api/v1/knowledge`。
- RAG Chat 接口：`POST /api/v1/chat`。
- LLM、Embedding、Vector Search、Hybrid Search 抽象层。
- React + TypeScript + Vite 前端。
- 前端页面：`/editor`、`/chat`、`/knowledge`。
- Markdown 编辑器：编辑、保存草稿、预览。
- AI Chat 页面：输入问题、展示回答、展示来源。
- Docker Compose 一键启动配置。
- GitHub Actions CI：后端测试、前端审计、前端构建。

## 项目结构

```text
.
├── .github/workflows/ci.yml
├── mindflow/
│   ├── backend/
│   │   ├── mindflow-domain/
│   │   ├── mindflow-application/
│   │   ├── mindflow-infrastructure/
│   │   ├── mindflow-ai/
│   │   └── mindflow-api/
│   ├── frontend/
│   ├── docs/
│   ├── docker-compose.yml
│   ├── CONTRIBUTING.md
│   └── README.md
└── README.md
```

## 如何开启项目

推荐使用 Docker 一键启动完整环境。

### 方式一：Docker 一键启动

前置要求：

- 已安装 Docker Desktop 或 Docker Engine。
- 已安装 Docker Compose。

启动：

```bash
cd mindflow
docker compose up --build
```

启动后访问：

- 前端页面：`http://localhost:5173`
- 后端接口：`http://localhost:8080`
- PostgreSQL：`localhost:5432`
- Redis：`81.70.47.98:6379`

停止：

```bash
cd mindflow
docker compose down
```

如果需要删除本地数据库数据卷：

```bash
cd mindflow
docker compose down -v
```

### 方式二：本地开发启动

前置要求：

- Java 21+
- Maven
- Node.js 22+
- npm
- PostgreSQL，建议带 pgvector
- Redis：`81.70.47.98:6379`

后端测试：

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml test
```

后端启动：

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml -pl mindflow-api -am spring-boot:run
```

前端安装依赖：

```bash
cd mindflow/frontend
npm ci
```

前端开发启动：

```bash
cd mindflow/frontend
npm run dev
```

前端构建：

```bash
cd mindflow/frontend
npm run build
```

## 快速上手

### 1. 打开 Markdown 编辑器

访问：

```text
http://localhost:5173/editor
```

你可以在左侧输入 Markdown，右侧会实时预览。点击“保存草稿”会先保存到浏览器本地。

### 2. 打开 AI Chat 页面

访问：

```text
http://localhost:5173/chat
```

输入问题后，前端会请求：

```http
POST /api/v1/chat
```

当前 RAG Chat 的搜索、LLM、Embedding 已经有应用层抽象和测试，真实 LLM HTTP 调用与 pgvector 检索适配器属于后续实现项。

### 3. 创建知识对象

后端接口：

```http
POST /api/v1/knowledge
Content-Type: application/json
```

示例请求：

```json
{
  "workspaceId": "00000000-0000-0000-0000-000000000001",
  "type": "NOTE",
  "title": "RAG 学习笔记",
  "content": "Retrieval Augmented Generation 可以让 AI 基于知识库回答问题。"
}
```

示例返回：

```json
{
  "id": "生成的知识 ID",
  "status": "CREATED"
}
```

## 常用命令

后端完整测试：

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml test
```

后端打包：

```bash
cd mindflow/backend
mvn --settings .mvn/settings.xml -pl mindflow-api -am package -DskipTests
```

前端依赖审计：

```bash
cd mindflow/frontend
npm audit --audit-level=moderate
```

前端构建：

```bash
cd mindflow/frontend
npm run build
```

Docker 配置检查：

```bash
cd mindflow
docker compose config
```

## 当前限制

v0.1 主要目标是把项目架构和主流程搭起来，因此有些能力已经定义了接口和测试，但还没有接真实生产适配器：

- 真实 LLM HTTP 调用。
- 真实 Embedding HTTP 调用。
- 真实 pgvector 相似度检索。
- PostgreSQL Full Text Search 真实适配。
- AI 任务后台轮询或队列消费。

这些都可以按后续 issue 继续小步实现。

## 更多文档

- 架构说明：[`mindflow/docs/architecture.md`](mindflow/docs/architecture.md)
- 部署说明：[`mindflow/docs/deployment.md`](mindflow/docs/deployment.md)
- 贡献规范：[`mindflow/CONTRIBUTING.md`](mindflow/CONTRIBUTING.md)
