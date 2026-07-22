# Auditoria de dependências — baseline

## npm

`npm audit --omit=dev` encontrou zero vulnerabilidades na árvore de produção.

`npm audit` encontrou 22 vulnerabilidades na árvore completa: 1 crítica, 10 altas, 7 moderadas e 4 baixas. Pacotes reportados incluem:

- `tar` — crítica, múltiplos advisories de crash/DoS/loop em parsing.
- `brace-expansion`, `fast-uri`, `http-proxy-middleware`, `immutable`, `js-yaml`, `piscina`, `shell-quote`, `vite` — altas.
- `@hono/node-server`, `hono`, `uuid` — moderadas, além de transitivos com severidades menores.
- `@babel/core`, `body-parser`, `esbuild` e cadeia Angular build/dev-server.

Advisories registrados pelo npm incluem `GHSA-w8wr-v893-vjvp`, `GHSA-23hp-3jrh-7fpw`, `GHSA-8x88-c5mf-7j5w`, `GHSA-gvwx-54wh-qm9j`, `GHSA-3jxr-9vmj-r5cp`, `GHSA-v2hh-gcrm-f6hx`, `GHSA-4c8g-83qw-93j6`, `GHSA-gcq2-9pq2-cxqm`, `GHSA-64mm-vxmg-q3vj`, `GHSA-x9g3-xrwr-cwfg` e `GHSA-v6wh-96g9-6wx3`.

Não foi executado `npm audit fix`: atualizar dependências é alteração de código/lock fora da Fase 0 e algumas correções sugeridas são breaking/fora do range.

## Maven

A árvore runtime foi resolvida com sucesso. Componentes centrais: Spring Boot 3.4.2, Spring Security 6.4.2, Hibernate 6.6.5.Final, PostgreSQL 42.7.5, Flyway 10.20.1, JJWT 0.12.5 e springdoc 2.3.0.

Não havia OWASP Dependency Check, Snyk ou outro scanner Maven configurado/disponível; portanto, ausência de achado Maven não significa ausência de vulnerabilidade.

## Imagem e SBOM

Não estavam disponíveis Trivy, Grype, Syft ou Docker Scout confirmado. Nenhum SBOM foi encontrado. O build resolveu imagens Temurin por digest durante a execução, mas o Dockerfile versiona tags flutuantes (`21-jdk-alpine`/`21-jre-alpine`).

## Gate recomendado

- Falha crítica: bloquear.
- Falha alta: bloquear ou exigir exceção formal com prazo.
- Separar dependências de produção e ferramentas de build na avaliação de risco, sem ignorar comprometimento de CI.
- Gerar SBOM e scan de imagem após build.
