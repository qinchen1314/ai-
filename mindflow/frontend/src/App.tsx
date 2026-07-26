type Route = '/editor' | '/chat' | '/knowledge';

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

      <section className="page-card">
        <p className="eyebrow">{currentRoute.path}</p>
        <h2>{currentRoute.label}</h2>
        <p>{currentRoute.description}</p>
      </section>
    </main>
  );
}

function normalizePath(pathname: string): Route {
  if (pathname === '/chat' || pathname === '/knowledge') {
    return pathname;
  }

  return '/editor';
}
