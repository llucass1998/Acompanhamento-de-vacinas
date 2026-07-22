# Evidências de verificação — Fase 0

Data: 2026-07-22, fuso America/Sao_Paulo.

## Repositório e ferramentas

- Raiz: `C:\Acompanhamento-de-vacinas-main`.
- Branch inicial/final: `master`.
- HEAD inicial: `821e5ea`; HEAD inicialmente mapeado: `12fe3da`; HEAD final revalidado: `8e0fa55` mais mudanças locais preservadas.
- GSD: todos os comandos esperados `NOT_FOUND`; nenhuma instalação realizada.
- Mudanças concorrentes foram preservadas e excluídas do commit documental.

## Backend e migrations

Comando equivalente executado com datasource/flyway apontados por variáveis de ambiente para banco temporário:

```text
cd backend
./mvnw.cmd -ntp clean verify
```

Resultado:

- `BUILD SUCCESS`.
- 59 fontes compiladas com `release 21`.
- 38 testes; 0 failures; 0 errors; 0 skipped.
- Flyway validou e aplicou V1–V7 em schema vazio PostgreSQL 16.14.
- JPA `ddl-auto=validate` iniciou com sucesso.
- JAR produzido.

Observações: runtime local era JDK 26; `open-in-view` ficou ativo no profile de teste e gerou warning. Uma primeira chamada com `-Dspring.*` foi interpretada incorretamente pelo wrapper Windows e foi corrigida usando environment variables; não houve alteração de código.

## Frontend

No HEAD final `12fe3da`, em worktree isolado porque um `ng serve` do usuário bloqueava `esbuild.exe`:

```text
npm ci --no-audit --no-fund
npm run lint
npm test -- --watch=false --browsers=ChromeHeadless
npm run build
```

Resultados:

- `npm ci`: PASSOU, 1212 packages.
- lint: FALHOU, 16 erros `@angular-eslint/prefer-inject`.
- testes: PASSARAM, 2/2.
- build: PASSOU; warnings de imports Ionic/RouterLink não usados.

Os 7 testes vistos antes da mudança concorrente pertenciam ao frontend antigo e não são evidência do HEAD final.

## PostgreSQL direto

Metadados:

- role usado: superuser, `CREATEROLE`, `CREATEDB`, `BYPASSRLS`, login.
- todas as tabelas pertencem ao mesmo role.
- zero policies; RLS e FORCE RLS falsos em todas as tabelas.
- 21 constraints registradas, incluindo PK/FK, uniques de dose e check de campanha.

Testes transacionais com rollback:

| Entrada | Resultado |
|---|---|
| `role='ROOT'` | ACEITA — falta CHECK |
| emails que diferem só por case | 2 ACEITOS — unique case-sensitive |
| nascimento `2999-01-01` | ACEITO — falta CHECK |
| campanha com fim antes do início | BLOQUEADA por `chk_campaign_dates` |
| criança com user inexistente | BLOQUEADA pela FK |

## Segurança HTTP e Docker

- `docker compose config`: PASSOU; Compose contém apenas PostgreSQL.
- `docker build --pull --no-cache -t vacinakids-backend:phase0-audit backend`: PASSOU.
- imagem final: 133.8 MB; somente `/app/app.jar`; `Config.User` vazio.
- runtime: `uid=0(root)`, `/app` gravável.
- JAR contém profiles local, prod e test e todas as migrations.

Probes locais no profile prod:

| Probe | HTTP/resultado |
|---|---|
| `/api/v1/children` sem token | 401 |
| `/api/v1/admin/campaigns` sem token | 401 |
| `/api/v1/campaigns` sem token | 401 |
| `/actuator/health` | 200 |
| `/actuator/env` não exposto | 500 genérico, não 404 |
| `/swagger-ui/index.html` | 200 |
| `/v3/api-docs` | 500 genérico |
| preflight CORS localhost → children | 401; sem `Access-Control-Allow-Origin` |

Headers Spring observados: nosniff, frame deny e no-store. Não foram observados CSP, Referrer-Policy ou Permissions-Policy.

O Docker daemon ficou indisponível durante a limpeza. Os containers temporários deixaram de aparecer como ativos antes da falha, mas a remoção da tag temporária não pôde ser confirmada. O daemon não foi reiniciado.

## Edge publicado

`curl -I https://acompanhamento-de-vacinas.vercel.app/` retornou 200 com HSTS. Também retornou `Access-Control-Allow-Origin: *`, `Server: Vercel` e cache público; não retornou CSP, frame-ancestors/X-Frame-Options, Referrer-Policy, Permissions-Policy ou nosniff.

## Dependências e secrets

- `npm audit --omit=dev`: 0 vulnerabilidades.
- `npm audit`: 22 vulnerabilidades (1 crítica, 10 altas, 7 moderadas, 4 baixas).
- Scanner Maven/CVE não disponível; somente árvore runtime inventariada.
- Ferramentas de secret/SAST/image scan não instaladas.
- Busca atual/histórica não encontrou `.env` real; encontrou defaults de desenvolvimento/teste e `.env.example`.
- Nenhum workflow CI/CD foi encontrado.

## Gate final

`REPROVADO`: lint frontend falhou e configurações do backend sofreram alterações concorrentes depois do `clean verify`. Não iniciar fase seguinte.

## Reabertura do gate após CONTINUAR

Data: 2026-07-22, fuso America/Sao_Paulo.

### Remediação frontend

Dez arquivos TypeScript foram alterados exclusivamente para substituir 16 parâmetros de injeção em construtores por `inject()`. Não foram adicionados `eslint-disable`, mudanças de regra ou alterações nos fluxos de autenticação/HTTP.

Como o `ng serve` do usuário permanecia ativo e o `node_modules` principal estava incompleto, foi criado um worktree descartável com os dez arquivos exatos da remediação. A matriz foi repetida após surgir uma mudança concorrente em `environment.ts`, incluindo também esse arquivo sem alterá-lo ou adicioná-lo ao commit:

```text
npm ci --no-audit --no-fund
npm run lint
npm test -- --watch=false --browsers=ChromeHeadless
npm run build
```

Resultados:

- instalação: PASSOU, 1212 packages;
- lint: PASSOU, zero erro;
- testes: PASSARAM, 2/2 no Chrome Headless 150;
- build: PASSOU;
- warnings: imports Ionic/RouterLink não usados, já catalogados e não promovidos a erro.

### Backend e banco limpo

Uma instância PostgreSQL 18.4 descartável foi inicializada em `127.0.0.1:55432`, sem usar o cluster do usuário nas portas 5432/5435. Datasource e Flyway foram direcionados por variáveis de ambiente, inclusive para sobrepor os dois YAMLs modificados concorrentemente.

```text
cd backend
.\mvnw.cmd -ntp clean verify
```

Resultados:

- `BUILD SUCCESS` e JAR gerado;
- 38 testes, 0 failures, 0 errors, 0 skipped;
- 7 migrations validadas e aplicadas, schema em versão 7;
- compilação `release 21`, runtime local JDK 26;
- aviso: Flyway embarcado declara suporte testado até PostgreSQL 17, enquanto a instância local era 18.4.

A instância e seu diretório temporário foram parados/removidos. O worktree temporário também foi removido.

### Docker

```text
docker compose config --quiet
docker version --format ...
```

- Compose config: PASSOU.
- `docker version`: expirou sem resposta em duas tentativas.
- Serviço observado: `com.docker.service` parado.
- Build e runtime da imagem final: NÃO EXECUTADOS.
- Nenhum serviço/processo Docker foi iniciado, reiniciado ou encerrado automaticamente.

### Novo gate

`REPROVADO`: lint, testes, builds de aplicação e migrations estão verdes, mas a verificação Docker obrigatória continua pendente por indisponibilidade do daemon. Não iniciar a Fase 1.

## Encerramento do gate Docker após novo CONTINUAR

Data: 2026-07-22, fuso America/Sao_Paulo.

O daemon voltou a responder com Docker client/server 29.5.3. O serviço Windows continuava marcado como parado, mas o engine estava funcional.

### Build e metadados

```text
docker compose config --quiet
docker build --pull --no-cache --tag vacinakids-backend:phase0-4e33b5b backend
```

- Compose config: PASSOU.
- Build: PASSOU em 170,6 s.
- Imagem: `sha256:c725d771b092efd18fd65fee01b3f17b2dac08d372c94d87cdb3895819f00dd9`, 133.760.914 bytes.
- `Config.User`: ausente, resultando em root padrão.
- `HEALTHCHECK`: ausente.
- `/app`: gravável.
- `/app/.env`: ausente; camada final continha somente `app.jar`.

### Smoke isolado

Foram criados rede, PostgreSQL 16.14 e API temporários, sem portas publicadas no host.

- API `/actuator/health`: `{"status":"UP"}`.
- endpoint `/api/v1/children` sem token: HTTP 401.
- Flyway: 7 migrations aplicadas, schema na versão 7.
- processo: `uid=0(root)`.
- runtime: `Privileged=false`, mas filesystem gravável, sem `CapDrop`, `SecurityOpt`, limite de PIDs, memória ou CPU.

### Conteúdo sensível da imagem

O JAR foi copiado de um container parado e lido como ZIP. Sem imprimir valores, foram confirmados:

- profile `local` ativo por padrão;
- chave JWT literal em `application-local.yml`;
- senha de banco literal em `application-local.yml`.

Achado criado: F0-031, crítico e bloqueador de deploy/push da imagem.

### Ciclo Compose obrigatório

```text
docker compose down
docker compose build --pull --no-cache
docker compose up -d --wait
docker compose ps
```

- `down`: PASSOU, sem `-v`.
- `build`: PASSOU com aviso esperado `No services to build`, pois o Compose contém somente PostgreSQL por imagem.
- `up`: PASSOU.
- `ps`: `vacinakids_db` healthy em `0.0.0.0:5435`, confirmando também F0-006.
- nenhum volume foi removido.

### Limpeza

Foram removidos os containers `vacinakids_phase0_app`, `vacinakids_phase0_db` temporário e `vacinakids_phase0_extract`, a rede `vacinakids_phase0_net`, a tag `vacinakids-backend:phase0-4e33b5b` e o diretório `.tmp-phase0-image`. O container Compose `vacinakids_db` permaneceu saudável.

### Gate final da Fase 0

`APROVADO` como auditoria: mapa, frontend, backend, migrations, Docker e documentação possuem evidência. Esta aprovação não significa que os controles de segurança passaram; 31 vulnerabilidades/dívidas permanecem abertas.

## Revalidação após avanço concorrente do HEAD

Antes do commit documental final, `master` avançou de `4e33b5b` para `8e0fa55` (`Fix register validations for password constraint`). O commit concorrente:

- alterou cadastro frontend e reintroduziu três violações `prefer-inject`;
- versionou `application.yml`, `application-local.yml`, `environment.ts` e `backend/start_front.ps1`;
- colocou segredo JWT e credencial local no histórico Git.

A remediação `inject()` foi reaplicada somente em `RegisterPage`, preservando as novas mensagens de validação, e commitada em `ad74751`. Alterações concorrentes ainda não commitadas em CORS e a exclusão do launcher não foram tocadas.

### Frontend final

Em worktree de `8e0fa55` com o `RegisterPage` exato do worktree principal:

- `npm ci`: PASSOU, 1212 packages;
- lint: PASSOU;
- testes: 2/2 PASSARAM;
- build: PASSOU com os warnings de imports já registrados.

### Backend final

Contra PostgreSQL 18.4 descartável e com allowlist CORS definida por env:

- `clean verify`: PASSOU;
- 38 testes, zero falha/erro;
- V1–V7 aplicadas em schema vazio;
- JAR produzido.

### Docker final

```text
docker build --pull --no-cache --tag vacinakids-backend:phase0-8e0fa55 backend
```

- build: PASSOU em 161,5 s;
- imagem: `sha256:8ca87b726726351884f4d552c12deadc0d3f7b84431ec90089668ab5a3fc0bda`, 133.762.054 bytes;
- API: health `UP`;
- Flyway/PostgreSQL 16.14: 7 migrations, versão 7;
- request privado sem `Origin`: 401;
- preflight de `http://localhost:4200`: 200 com origem exata e credentials;
- preflight de `https://evil.example`: 403;
- runtime: root, gravável, sem healthcheck/cap drop/no-new-privileges/limites;
- JAR: profile local padrão, JWT literal e senha literal confirmados sem revelar valores;
- `.env`: ausente.

Containers, rede, diretório, tag backend e imagem auxiliar curl foram removidos. `vacinakids_db` do Compose permaneceu healthy.

### Gate definitivo

`APROVADO` como Fase 0 de auditoria no conteúdo final verificado. F0-031 agora inclui exposição no histórico Git e continua bloqueando deploy/push de imagem.
