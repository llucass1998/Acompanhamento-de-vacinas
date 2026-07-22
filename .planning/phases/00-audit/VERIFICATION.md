# Evidências de verificação — Fase 0

Data: 2026-07-22, fuso America/Sao_Paulo.

## Repositório e ferramentas

- Raiz: `C:\Acompanhamento-de-vacinas-main`.
- Branch inicial/final: `master`.
- HEAD inicial: `821e5ea`; HEAD final mapeado: `12fe3da`.
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
