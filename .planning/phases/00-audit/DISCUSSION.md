# Discussão — Fase 0

## Objetivo e limites

Mapear o sistema real e estabelecer um baseline verificável sem alterar código de aplicação. A fase não corrige vulnerabilidades e não inicia modelo de ameaças.

## Entradas lidas

- `README.md`, documentação existente, `package.json`, `angular.json`, profiles Spring, `pom.xml`.
- Todos os controllers, services, repositories, DTOs, entidades, configurações de segurança e migrations.
- Frontend: rotas, guard, interceptor, auth, services HTTP, models e páginas.
- Dockerfile, Compose, Vercel, testes, logs, histórico Git e dependências.

## Riscos de execução

- Banco de desenvolvimento já estava ativo: mitigado com PostgreSQL temporário em porta separada.
- Testes poderiam apagar dados: nunca foram apontados para o banco existente.
- Mudanças concorrentes poderiam invalidar o mapa: ocorreram duas vezes; HEAD foi reidentificado e o frontend foi reauditado.
- `ng serve` concorrente bloqueou `npm ci`: mitigado com worktree temporário destacado, sem encerrar processo do usuário.
- Docker ficou indisponível na limpeza: não foi reiniciado automaticamente.

## Arquivos principais envolvidos

- Frontend: `src/app/**`, `src/environments/**`, `angular.json`, `package*.json`, `vercel.json`.
- Backend: `backend/src/main/**`, `backend/src/test/**`, `backend/pom.xml`, `backend/Dockerfile`.
- Banco: `backend/src/main/resources/db/migration/**`, `docker-compose.yml`.
- Planejamento/documentação: `.planning/**`, `docs/SECURITY_*.md`, `docs/DATABASE_SECURITY.md`.

## Perguntas críticas

Nenhuma pergunta impediu a auditoria. Permanecem decisões futuras que não devem ser assumidas na Fase 0: domínio real da API, política web versus Capacitor, provedor de edge, estratégia de roles/RLS e RPO/RTO.

## Decisão

Executar o mapa manual equivalente ao GSD porque os comandos GSD não estão disponíveis. Classificar a fase como reprovada se qualquer gate do baseline estiver vermelho. Não tocar nas alterações concorrentes.
