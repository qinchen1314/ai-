# Contributing

MindFlow development follows an issue-by-issue workflow.

Before making changes:

- Read the current issue and existing architecture.
- Keep changes scoped to the requested module.
- Do not modify unrelated files or modules.
- Add tests when behavior or contracts are introduced.
- Explain what changed, why it changed, impact, and verification.

Core architectural rules:

- Domain code should not depend on Spring.
- AI processing should be designed as asynchronous work.
- AI-generated answers must support source tracking.
- MVP features should stay simple and extensible.
