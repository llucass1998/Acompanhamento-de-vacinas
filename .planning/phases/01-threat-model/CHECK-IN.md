# CHECK-IN GSD DE SEGURANÇA — FASE 1

Status: APROVADA

### Objetivo da fase

Registrar fronteiras de confiança, fluxos, abusos, controles independentes e risco residual do Vacina Kids.

### Tarefas planejadas

- [x] Discussão da fase.
- [x] Plano com tarefas, riscos e critérios.
- [x] Modelo Mermaid dos fluxos.
- [x] Casos de abuso obrigatórios.
- [x] Matriz com três camadas para riscos críticos.

### Controles implementados

Nenhum controle de código foi implementado nesta fase. O resultado é a especificação verificável para as fases seguintes.

### Camadas protegidas

Todas foram analisadas: frontend, edge, Spring Security, controller/DTO, service, repository, PostgreSQL, infraestrutura, CI/CD e observabilidade.

### Testes positivos

- Revisão documental contra o mapa da Fase 0: PASSOU.
- Todos os fluxos de confiança exigidos documentados: PASSOU.

### Testes negativos

- Casos de abuso TM-01 a TM-18 registrados com risco, controles e detecção: PASSOU documentalmente.
- Execução de ataques: PENDENTE, conforme fases de implementação.

### Testes de regressão

- Backend: NÃO EXECUTADO (fase documental; baseline da Fase 0 permanece válido).
- Frontend: NÃO EXECUTADO.
- PostgreSQL: NÃO EXECUTADO.
- Docker: NÃO EXECUTADO.

### Build

- Backend: NÃO EXECUTADO.
- Frontend: NÃO EXECUTADO.
- Docker: NÃO EXECUTADO.

### Banco

- Migrations: NÃO EXECUTADAS nesta fase.
- Constraints: NÃO APLICÁVEL.
- RLS: NÃO APLICÁVEL; ausência permanece risco documentado.

### Vulnerabilidades encontradas

Nenhuma nova vulnerabilidade técnica foi introduzida. Os 31 achados da Fase 0 permanecem abertos; F0-031 continua bloqueando deploy/push.

### Evidências

- `docs/THREAT_MODEL.md`.
- `.planning/phases/01-threat-model/DISCUSSION.md`.
- `.planning/phases/01-threat-model/PLAN.md`.
- `STATE.md`, checklist e testes atualizados.

### Commit

`git commit -m "docs: cria modelo de ameacas do vacina kids"`

### Estado do GSD

- STATE.md atualizado.
- ROADMAP e checklist atualizados conforme resultado documental.
- Plano da fase arquivado no diretório da fase.

### Próxima fase

Fase 2 — frontend Angular/Ionic — aguarda novo `CONTINUAR`.
