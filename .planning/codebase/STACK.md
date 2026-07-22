# Stack observada

## Frontend

- Angular `20.3.25`, CLI/build `20.3.28`, standalone components.
- Ionic Angular `^8.0.0`.
- TypeScript `~5.9.0`, RxJS `~7.8.0`, Zone.js `~0.15.0`.
- Capacitor `8.x` sem plataforma nativa versionada no repositório.
- Jasmine 5/Karma 6/ChromeHeadless; ESLint 9 e Angular ESLint 20.
- Build em `www/`, deploy configurado no Vercel.

## Backend

- Java source/target/release 21; build local executado com JDK 26.0.1.
- Spring Boot `3.4.2`.
- Spring Web/MVC, Security `6.4.2`, Data JPA, Validation e Actuator.
- Hibernate `6.6.5.Final`, HikariCP `5.1.0`.
- PostgreSQL driver `42.7.5`.
- Flyway `10.20.1`.
- JJWT `0.12.5` com chave HMAC simétrica.
- Springdoc `2.3.0` e Swagger UI `5.10.3`.
- JUnit 5, Mockito, MockMvc, Spring Security Test e dependências Testcontainers.

## Banco e infraestrutura

- PostgreSQL `16-alpine`; ambiente temporário validado em PostgreSQL 16.14.
- Docker Desktop/Engine 29.5.3 e Compose 5.1.4 no início da auditoria.
- Dockerfile backend multi-stage em Temurin 21 JDK/JRE Alpine.
- Compose possui apenas o serviço `postgres` e volume persistente.
- Frontend publicado no Vercel; não há proxy/edge versionado além de `vercel.json`.

## Ferramentas e cadeia de suprimentos

- Maven Wrapper 3.3.4 baixa Maven 3.9.16, sem checksum em `maven-wrapper.properties`.
- `package-lock.json` está versionado.
- Não encontrados: GSD, gitleaks, trufflehog, trivy, grype, syft, semgrep ou snyk.
- Não há workflow GitHub Actions, Dependabot, SBOM ou scanner configurado.
