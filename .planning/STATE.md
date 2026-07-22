# Estado GSD

## Fase atual

- Fase: `00-audit`.
- Status: `APROVADA` como auditoria completa; esta aprovação não declara o sistema seguro.
- Último checkpoint aprovado: Fase 0 — mapa, baseline, Docker e registro de vulnerabilidades verificados.
- Próxima fase: Fase 1 aguarda `CONTINUAR`; não iniciar automaticamente.

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
- HEAD inicialmente mapeado: `12fe3da`; documentação inicial em `108f983`; remediação em `4e33b5b`.
- HEAD concorrente verificado: `8e0fa55`, que alterou cadastro e versionou configurações locais, segredo JWT, credencial de banco e launcher. A auditoria não criou esse commit.
- HEAD da correção de baseline: `ad74751`, reaplicando somente `inject()` após a regressão concorrente de lint.
- Mudanças concorrentes ainda não commitadas: CORS em `SecurityConfig.java`, segunda origem em `application-local.yml` e exclusão de `backend/start_front.ps1`. Elas foram preservadas e não serão incluídas no commit documental.

## Testes e builds

- Backend revalidado: 38 testes, `clean verify` e JAR `PASSARAM` contra PostgreSQL 18.4 temporário em banco vazio; target Java 21, runtime local JDK 26.
- Flyway: V1–V7 `PASSARAM` em schema vazio; houve aviso porque a versão embarcada declara suporte testado somente até PostgreSQL 17.
- Frontend no estado anterior a `12fe3da`: 7 testes, lint e build passaram, mas esse resultado ficou obsoleto após a mudança concorrente.
- Frontend após `8e0fa55` e reaplicação de `inject()` em RegisterPage, em worktree isolado: `npm ci`, lint, 2 testes e build `PASSARAM`; permanecem warnings de imports Ionic não usados.
- Dockerfile backend: build final de `8e0fa55` + mudanças locais preservadas passou em 161,5 s; imagem de 133.762.054 bytes. Runtime observado como `uid=0(root)`, `/app` gravável e sem `HEALTHCHECK`.
- HTTP local: recurso protegido 401; USER/ADMIN sem token 401; health 200; Swagger UI 200; OpenAPI JSON 500; CORS preflight 401 sem allow-origin.
- Dependências npm: produção, 0 vulnerabilidades; árvore completa, 22 (1 crítica, 10 altas, 7 moderadas, 4 baixas).
- Compose: config, `down`, `build`, `up -d --wait` e `ps` passaram; não há serviço com `build:` no Compose. O volume não foi removido e `vacinakids_db` voltou saudável.
- Smoke Docker isolado: API `UP`, endpoint privado sem token 401 e V1–V7 aplicadas em PostgreSQL 16.14 sem portas temporárias publicadas.
- CORS concorrente: preflight permitido retornou 200 com origem exata; origem maliciosa retornou 403; chamada sem Origin permaneceu 401. A implementação continua não commitada e exige `APP_CORS_ALLOWED_ORIGINS` no profile prod.
- Conteúdo da imagem: `.env` ausente, mas profile local padrão, chave JWT literal e senha de banco literal foram confirmados dentro do JAR sem revelar valores.

## Decisões

- A remediação autorizada alterou apenas o padrão de injeção Angular, migrando 16 parâmetros de construtor para `inject()` sem mudar fluxos funcionais ou controles de segurança.
- Frontend é tratado como não confiável; guard e storage não contam como controle.
- Banco temporário isolado foi usado para não tocar no banco de desenvolvimento existente.
- Nenhuma correção de segurança foi misturada com a auditoria.
- A mitigação CORS foi criada concorrentemente pelo usuário; a auditoria apenas a testou e não a adicionará ao commit.
- Docker não será reiniciado automaticamente após o daemon ter ficado indisponível.
- O ciclo Compose foi executado somente após `CONTINUAR`, sem `-v`; containers/rede/tag/diretório temporários foram removidos e o banco de desenvolvimento foi restaurado saudável.

## Bloqueios

- Nenhum bloqueio para encerrar a auditoria da Fase 0.
- Deploy, push de imagem e uso produtivo permanecem bloqueados pelo achado F0-031: segredos literais e profile local padrão são empacotados no JAR.
- O segredo/credencial já constam no commit concorrente `8e0fa55`; removê-los e rotacioná-los exige autorização específica e permanece pendente.
- `SecurityConfig.java`, a alteração adicional de `application-local.yml` e a exclusão de `backend/start_front.ps1` continuam fora do commit documental.

## Vulnerabilidades abertas

- Os 31 achados F0-001–F0-031 continuam registrados em `.planning/codebase/CONCERNS.md`; a remediação de lint não é tratada como correção de segurança.
- Contratos frontend/backend incompatíveis e URL de produção placeholder continuam abertos para fase apropriada.

## Próxima ação autorizada

Aguardar. Somente um novo `CONTINUAR` autoriza iniciar a discussão e o plano da Fase 1 — modelo de ameaças.
