# Estado GSD

## Fase atual

- Fase: `00-audit`.
- Status: `REPROVADA`.
- Último checkpoint aprovado: nenhum neste ciclo de segurança.
- Próxima fase: bloqueada; não iniciar Fase 1.

## GSD disponível

Nenhum comando `gsd`, `gsd-map-codebase`, `gsd-new-project`, `gsd-discuss-phase`, `gsd-plan-phase`, `gsd-execute-phase`, `gsd-verify-work` ou `gsd-ship` foi encontrado no `PATH` ou no repositório. Nada foi instalado.

Correspondência manual usada:

| Fluxo esperado | Equivalente registrado |
|---|---|
| map codebase | `.planning/codebase/*` e `phases/00-audit/AUDIT_REPORT.md` |
| new/update project | `PROJECT.md`, `REQUIREMENTS.md`, `SECURITY_REQUIREMENTS.md` |
| discuss phase | `phases/00-audit/DISCUSSION.md` |
| plan phase | `phases/00-audit/PLAN.md` |
| execute phase | inspeções e comandos em `VERIFICATION.md` |
| verify work | `phases/00-audit/VERIFICATION.md` e `docs/SECURITY_TESTS.md` |
| ship/checkpoint | commit documental e check-in; sem avanço automático |

## Repositório observado

- Raiz: `C:\Acompanhamento-de-vacinas-main`.
- Branch: `master`.
- Início da auditoria: `821e5ea`, worktree limpo.
- HEAD final mapeado: `12fe3da`.
- Mudanças concorrentes: integração frontend foi commitada durante a auditoria; depois surgiram alterações locais em `application.yml` e `application-local.yml`. Elas pertencem ao usuário, não foram alteradas nem incluídas no commit documental.

## Testes e builds

- Backend: 38 testes, `clean verify` e JAR `PASSARAM` contra PostgreSQL 16 temporário em banco vazio; target Java 21, runtime local JDK 26.
- Flyway: V1–V7 `PASSARAM` em schema vazio.
- Frontend no estado anterior a `12fe3da`: 7 testes, lint e build passaram, mas esse resultado ficou obsoleto após a mudança concorrente.
- Frontend em `12fe3da`, worktree isolado: `npm ci` passou; lint falhou com 16 erros; 2 testes passaram; build passou com warnings de imports não usados.
- Dockerfile backend: build `--pull --no-cache` passou; runtime observado como `uid=0(root)` e `/app` gravável.
- HTTP local: recurso protegido 401; USER/ADMIN sem token 401; health 200; Swagger UI 200; OpenAPI JSON 500; CORS preflight 401 sem allow-origin.
- Dependências npm: produção, 0 vulnerabilidades; árvore completa, 22 (1 crítica, 10 altas, 7 moderadas, 4 baixas).

## Decisões

- Nenhum código de aplicação foi alterado na Fase 0.
- Frontend é tratado como não confiável; guard e storage não contam como controle.
- Banco temporário isolado foi usado para não tocar no banco de desenvolvimento existente.
- Nenhuma correção de segurança foi misturada com a auditoria.
- Docker não será reiniciado automaticamente após o daemon ter ficado indisponível.

## Bloqueios

1. Lint frontend vermelho.
2. Contratos frontend/backend incompatíveis e URL de produção placeholder.
3. Configurações do backend sendo alteradas concorrentemente e ainda não verificadas no estado final.
4. Docker daemon indisponível ao final da limpeza; remoção da tag temporária não foi confirmada.

## Próxima ação autorizada

Estabilizar o worktree e corrigir exclusivamente o baseline da Fase 0, com novo plano e nova verificação. `CONTINUAR` não pode promover a Fase 1 enquanto o gate permanecer vermelho.
