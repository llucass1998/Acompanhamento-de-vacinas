# CHECK-IN GSD DE SEGURANÇA — FASE 2

Status: APROVADA

### Objetivo da fase

Reduzir riscos no cliente sem tratá-lo como autoridade de segurança.

### Tarefas planejadas

- [x] Remover persistência web de access/refresh tokens.
- [x] Restringir interceptor à URL exata da API.
- [x] Evitar logout/redirect por 401 de origem externa.
- [x] Aplicar CSP e headers equivalentes no documento.
- [x] Executar regressão frontend.

### Controles implementados

- Tokens mantidos somente em memória.
- Bearer limitado à API configurada.
- CSP com `frame-ancestors 'none'`, `object-src 'none'` e `script-src 'self'`.
- `nosniff`, referrer policy e permissions policy.

### Camadas protegidas

- Frontend: controles de storage, interceptor e CSP.
- Edge/proxy: permanece pendente para headers definitivos.
- Backend: permanece autoridade independente.

### Testes positivos

- `npm run lint`: PASSOU.
- `npm test -- --watch=false --browsers=ChromeHeadless`: 2 testes PASSARAM.
- `npm run build`: PASSOU, com warnings existentes de imports Ionic não usados.

### Testes negativos

- Token não é lido/escrito em `localStorage` ou `sessionStorage`: PASSOU por inspeção.
- URL externa não recebe Authorization: PASSOU por inspeção do allowlist.
- 401 externo não encerra sessão: PASSOU por inspeção.

### Build

- Frontend: PASSOU.
- Backend/Docker: não aplicável nesta fase.

### Banco

- Não aplicável.

### Vulnerabilidades encontradas

Nenhuma nova. F0-031 e demais achados permanecem abertos.

### Commit

`git commit -m "security: endurece armazenamento e interceptor angular"`

### Estado do GSD

- Discussão, plano e check-in arquivados.
- Alterações concorrentes preservadas.

### Próxima fase

Fase 3 — Spring Security — aguarda novo `CONTINUAR`.
