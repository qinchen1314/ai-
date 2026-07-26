import { useMemo, useState } from 'react';

type Route = '/editor' | '/chat' | '/knowledge';

const defaultMarkdown = `# MindFlow 知识笔记

用 Markdown 写下你的想法，右侧会即时预览。

- 支持标题
- 支持列表
- 支持代码块

\`\`\`
Question -> Search -> Context -> LLM -> Answer
\`\`\`
`;

const routes: Array<{ path: Route; label: string; description: string }> = [
  {
    path: '/editor',
    label: 'Markdown 编辑器',
    description: '创建、整理和预览知识内容。',
  },
  {
    path: '/chat',
    label: 'AI Chat',
    description: '基于你的知识库进行 RAG 问答。',
  },
  {
    path: '/knowledge',
    label: '知识库',
    description: '浏览已沉淀的知识对象和来源。',
  },
];

export function App() {
  const currentPath = normalizePath(window.location.pathname);
  const currentRoute = routes.find((route) => route.path === currentPath) ?? routes[0];

  return (
    <main className="app-shell">
      <aside className="sidebar" aria-label="MindFlow navigation">
        <div>
          <p className="eyebrow">MindFlow v0.1</p>
          <h1>AI 知识大脑</h1>
        </div>
        <nav className="nav-links">
          {routes.map((route) => (
            <a
              aria-current={route.path === currentRoute.path ? 'page' : undefined}
              className={route.path === currentRoute.path ? 'active' : undefined}
              href={route.path}
              key={route.path}
            >
              {route.label}
            </a>
          ))}
        </nav>
      </aside>

      {currentRoute.path === '/editor' ? (
        <MarkdownEditorPage />
      ) : (
        <section className="page-card">
          <p className="eyebrow">{currentRoute.path}</p>
          <h2>{currentRoute.label}</h2>
          <p>{currentRoute.description}</p>
        </section>
      )}
    </main>
  );
}

function normalizePath(pathname: string): Route {
  if (pathname === '/chat' || pathname === '/knowledge') {
    return pathname;
  }

  return '/editor';
}

function MarkdownEditorPage() {
  const [markdown, setMarkdown] = useState(() => localStorage.getItem('mindflow.editor.markdown') ?? defaultMarkdown);
  const [savedAt, setSavedAt] = useState<string | null>(() => localStorage.getItem('mindflow.editor.savedAt'));
  const previewBlocks = useMemo(() => parseMarkdown(markdown), [markdown]);

  function saveDraft() {
    const timestamp = new Date().toISOString();
    localStorage.setItem('mindflow.editor.markdown', markdown);
    localStorage.setItem('mindflow.editor.savedAt', timestamp);
    setSavedAt(timestamp);
  }

  return (
    <section className="editor-page">
      <div className="editor-header">
        <div>
          <p className="eyebrow">/editor</p>
          <h2>Markdown 编辑器</h2>
          <p>编辑、保存、预览知识内容。当前版本先保存到浏览器本地。</p>
        </div>
        <button className="primary-button" type="button" onClick={saveDraft}>
          保存草稿
        </button>
      </div>

      <div className="editor-grid">
        <label className="editor-panel">
          <span>编辑</span>
          <textarea
            aria-label="Markdown 内容"
            value={markdown}
            onChange={(event) => setMarkdown(event.target.value)}
          />
        </label>

        <div className="preview-panel">
          <span>预览</span>
          <article className="markdown-preview">
            {previewBlocks.length === 0 ? <p className="empty-state">写点东西，预览会出现在这里。</p> : null}
            {previewBlocks.map((block) => renderBlock(block))}
          </article>
        </div>
      </div>

      <p className="save-state">
        {savedAt ? `上次保存：${new Date(savedAt).toLocaleString()}` : '还没有保存过。'}
      </p>
    </section>
  );
}

type MarkdownBlock =
  | { type: 'heading'; level: 1 | 2 | 3; text: string; key: string }
  | { type: 'paragraph'; text: string; key: string }
  | { type: 'list'; items: string[]; key: string }
  | { type: 'code'; text: string; key: string };

function parseMarkdown(markdown: string): MarkdownBlock[] {
  const blocks: MarkdownBlock[] = [];
  const lines = markdown.split(/\r?\n/);
  let paragraph: string[] = [];
  let list: string[] = [];
  let code: string[] | null = null;

  function flushParagraph(index: number) {
    if (paragraph.length > 0) {
      blocks.push({ type: 'paragraph', text: paragraph.join(' '), key: `p-${index}` });
      paragraph = [];
    }
  }

  function flushList(index: number) {
    if (list.length > 0) {
      blocks.push({ type: 'list', items: list, key: `list-${index}` });
      list = [];
    }
  }

  for (const [index, line] of lines.entries()) {
    if (line.trim().startsWith('```')) {
      flushParagraph(index);
      flushList(index);
      if (code === null) {
        code = [];
      } else {
        blocks.push({ type: 'code', text: code.join('\n'), key: `code-${index}` });
        code = null;
      }
      continue;
    }

    if (code !== null) {
      code.push(line);
      continue;
    }

    const trimmed = line.trim();
    if (!trimmed) {
      flushParagraph(index);
      flushList(index);
      continue;
    }

    const heading = /^(#{1,3})\s+(.+)$/.exec(trimmed);
    if (heading) {
      flushParagraph(index);
      flushList(index);
      blocks.push({
        type: 'heading',
        level: heading[1].length as 1 | 2 | 3,
        text: heading[2],
        key: `h-${index}`,
      });
      continue;
    }

    if (trimmed.startsWith('- ')) {
      flushParagraph(index);
      list.push(trimmed.slice(2));
      continue;
    }

    flushList(index);
    paragraph.push(trimmed);
  }

  flushParagraph(lines.length);
  flushList(lines.length);
  if (code !== null && code.length > 0) {
    blocks.push({ type: 'code', text: code.join('\n'), key: 'code-open' });
  }

  return blocks;
}

function renderBlock(block: MarkdownBlock) {
  if (block.type === 'heading') {
    const HeadingTag = `h${block.level}` as const;
    return <HeadingTag key={block.key}>{block.text}</HeadingTag>;
  }

  if (block.type === 'list') {
    return (
      <ul key={block.key}>
        {block.items.map((item, index) => (
          <li key={`${block.key}-${index}`}>{item}</li>
        ))}
      </ul>
    );
  }

  if (block.type === 'code') {
    return (
      <pre key={block.key}>
        <code>{block.text}</code>
      </pre>
    );
  }

  return <p key={block.key}>{block.text}</p>;
}
