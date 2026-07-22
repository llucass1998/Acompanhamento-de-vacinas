# Estado GSD

## Fase atual

- Fase: `00-audit`.
- Status: `REPROVADA` após remediação parcial; código, testes, build e migrations estão verdes, mas o gate Docker não pôde ser reexecutado.
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
- HEAD final mapeado: `12fe3da`; documentação da auditoria em `108f983`.
- Mudanças concorrentes: integração frontend foi commitada durante a auditoria; depois surgiram alterações locais em `application.yml`, `application-local.yml` e `environment.ts` para configuração local/porta 8081, além do launcher não rastreado `backend/start_front.ps1`. Elas pertencem ao usuário, não foram alteradas nem incluídas nos commits da auditoria.

## Testes e builds

- Backend revalidado: 38 testes, `clean verify` e JAR `PASSARAM` contra PostgreSQL 18.4 temporário em banco vazio; target Java 21, runtime local JDK 26.
- Flyway: V1–V7 `PASSARAM` em schema vazio; houve aviso porque a versão embarcada declara suporte testado somente até PostgreSQL 17.
- Frontend no estado anterior a `12fe3da`: 7 testes, lint e build passaram, mas esse resultado ficou obsoleto após a mudança concorrente.
- Frontend após remediação, em worktree isolado: `npm ci`, lint, 2 testes e build `PASSARAM`; permanecem warnings de imports Ionic não usados.
- Dockerfile backend: build `--pull --no-cache` passou; runtime observado como `uid=0(root)` e `/app` gravável.
- HTTP local: recurso protegido 401; USER/ADMIN sem token 401; health 200; Swagger UI 200; OpenAPI JSON 500; CORS preflight 401 sem allow-origin.
- Dependências npm: produção, 0 vulnerabilidades; árvore completa, 22 (1 crítica, 10 altas, 7 moderadas, 4 baixas).
- Compose: `docker compose config --quiet` passou. O daemon Docker não respondeu a duas tentativas de `docker version`; o serviço `com.docker.service` estava parado, portanto build/runtime não foram reexecutados.

## Decisões

- A remediação autorizada alterou apenas o padrão de injeção Angular, migrando 16 parâmetros de construtor para `inject()` sem mudar fluxos funcionais ou controles de segurança.
- Frontend é tratado como não confiável; guard e storage não contam como controle.
- Banco temporário isolado foi usado para não tocar no banco de desenvolvimento existente.
- Nenhuma correção de segurança foi misturada com a auditoria.
- Docker não será reiniciado automaticamente após o daemon ter ficado indisponível.

## Bloqueios

1. Docker daemon indisponível; imagem e runtime do estado final não puderam ser revalidados.
2. `application.yml`, `application-local.yml` e `environment.ts` continuam modificados pelo usuário; os YAMLs contêm credenciais/chave local hardcoded. Permanecem fora do commit; backend foi isolado por overrides e frontend foi revalidado incluindo o `environment.ts` corrente.

## Vulnerabilidades abertas

- Os 30 achados F0-001–F0-030 continuam registrados em `.planning/codebase/CONCERNS.md`; a remediação de lint não é tratada como correção de segurança.
- Contratos frontend/backend incompatíveis e URL de produção placeholder continuam abertos para fase apropriada.

## Próxima ação autorizada

O usuário deve iniciar/restaurar o Docker Desktop fora deste fluxo e enviar `CONTINUAR`. A próxima execução revalidará somente build/runtime Docker e fechará o check-in da Fase 0; a Fase 1 seguirá bloqueada até um novo `CONTINUAR` após checkpoint aprovado.
