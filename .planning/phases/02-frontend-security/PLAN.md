# Plano — Fase 2

| Tarefa | Resultado | Verificação |
|---|---|---|
| Token storage | access/refresh somente em memória | busca sem persistência de tokens + build |
| Interceptor | allowlist exata da API e logout só para 401 da API | lint, testes e inspeção |
| XSS/headers | CSP, frame-ancestors, nosniff, referrer e permissions policy | build e inspeção de `index.html` |
| Regressão | preservar rotas, serviços e fluxo existente | lint, testes Karma e build |

Critério: nenhum token em localStorage/sessionStorage/URL e nenhum bearer para origem externa.
