# CHECK-IN GSD DE SEGURANÇA — FASE 0

Status: REPROVADA

## Objetivo da fase

Mapear integralmente o sistema, registrar arquitetura e riscos reais e estabelecer um baseline verificável antes de qualquer hardening. A reabertura corrigiu somente o lint necessário para restaurar o gate de qualidade.

## Tarefas planejadas

- [x] Confirmar raiz, branch, status, frontend/backend e disponibilidade GSD.
- [x] Mapear frontend, backend, banco, autenticação, autorização, tokens, Docker, edge e dependências.
- [x] Executar backend, frontend, migrations e probes de auditoria.
- [x] Criar documentos GSD e documentação de segurança não vazia.
- [x] Corrigir os 16 erros de lint sem desativar regras.
- [x] Reexecutar frontend, backend e migrations em ambientes isolados.
- [ ] Reexecutar build e runtime Docker no estado final.

## Controles implementados

- Nenhum controle de segurança novo foi implementado na fase de auditoria.
- O gate ESLint foi restaurado por migração mecânica para `inject()`.
- Isolamento operacional: worktree e PostgreSQL descartáveis impediram interferência no `ng serve` e no banco do usuário.

## Camadas protegidas

Não aplicável como implementação de segurança; todas as onze camadas foram mapeadas e seus controles/lacunas foram registrados.

## Testes positivos

- Frontend install/lint/unit/build: PASSARAM.
- Backend `clean verify`: PASSOU, 38/38.
- Flyway V1–V7 em banco vazio: PASSARAM.
- Compose config: PASSOU.

## Testes negativos

- API protegida sem token: esperado 401; obtido 401; Spring Security bloqueou.
- USER em endpoint ADMIN: esperado 403; obtido 403; rota/método bloquearam.
- Criança de outro usuário: esperado bloqueio; obtido 404; service/repository bloquearam.
- Role arbitrária, email case-variant e nascimento futuro direto no banco: bloqueio esperado, mas entradas aceitas; constraints ausentes.
- Runtime container não root: esperado UID não zero; obtido anteriormente `uid=0`; hardening ausente.

## Testes de regressão

- Backend: PASSOU.
- Frontend: PASSOU.
- PostgreSQL: PASSOU.
- Docker: FALHOU — daemon indisponível para revalidação final.

## Build

- Backend: PASSOU.
- Frontend: PASSOU.
- Docker: FALHOU — não executado porque o daemon não respondeu.

## Banco

- Migrations: PASSARAM.
- Constraints: VALIDADAS; lacunas continuam abertas em F0-017.
- RLS: NÃO APLICÁVEL como verificação positiva; inexiste e está registrado em F0-005.

## Vulnerabilidades encontradas

- CRÍTICA — PostgreSQL/Compose — runtime superuser/owner/BYPASSRLS — aberta, F0-001.
- CRÍTICA — interceptor frontend — Bearer enviado para qualquer URL HTTP — aberta, F0-002.
- CRÍTICA — dependências dev npm — 22 achados, um crítico — aberta, F0-003.
- ALTA — tokens web em localStorage — aberta, F0-004.
- ALTA — banco sem RLS, container root, DB público e CI ausente — abertas.
- Registro completo: `.planning/codebase/CONCERNS.md`, F0-001–F0-030.

## Evidências

- `npm ci --no-audit --no-fund`, `npm run lint`, Karma ChromeHeadless e `npm run build`.
- `.\mvnw.cmd -ntp clean verify`: 38 testes e `BUILD SUCCESS`.
- Flyway: 7 migrations aplicadas, versão final 7 em PostgreSQL 18.4 descartável.
- HTTP 401/403/404, constraints diretas, UID do container e headers registrados em `VERIFICATION.md`.
- `docker compose config --quiet`: sucesso; `docker version`: timeout.
- Logs citados estão sanitizados; nenhuma senha ou token real foi registrado na documentação.

## Commit

```text
git commit -m "chore: restaura baseline da fase 0"
```

## Estado do GSD

- `STATE.md` atualizado.
- `ROADMAP.md` atualizado.
- `SECURITY_CHECKLIST.md` atualizado.
- `SECURITY_TESTS.md` atualizado.
- Plano de remediação arquivado.

## Próxima fase

Fase 1 permanece bloqueada. Primeiro é necessário disponibilizar o daemon Docker e enviar `CONTINUAR` para concluir somente a verificação Docker da Fase 0.
