# Registro priorizado de vulnerabilidades e dívidas

Estados: `ABERTA`, `MITIGAÇÃO PARCIAL` ou `BLOQUEIO DE BASELINE`. Nenhuma foi corrigida na Fase 0.

| ID | Severidade | Local | Risco/evidência | Fase | Estado |
|---|---|---|---|---|---|
| F0-001 | CRÍTICA | PostgreSQL/Compose | usuário da aplicação é o `POSTGRES_USER` criado como superuser, owner, `CREATEROLE`, `CREATEDB` e `BYPASSRLS` | 7 | ABERTA |
| F0-002 | CRÍTICA | `JwtInterceptor` | envia Bearer a qualquer URL iniciada por `http`, permitindo exfiltração para origem externa | 2 | ABERTA |
| F0-003 | CRÍTICA | dependências dev npm | 22 achados, incluindo `tar` crítico; não há gate/scan CI | 11 | ABERTA |
| F0-004 | ALTA | frontend auth | access e refresh token em `localStorage`; XSS compromete a sessão longa | 2/8 | ABERTA |
| F0-005 | ALTA | PostgreSQL | nenhuma tabela possui RLS/policy; consulta sem owner retorna tudo permitido ao role | 7 | ABERTA |
| F0-006 | ALTA | Docker/DB | PostgreSQL publicado em todas as interfaces na porta 5435 | 9 | ABERTA |
| F0-007 | ALTA | backend Dockerfile | processo final executa `uid=0(root)` e `/app` é gravável | 9 | ABERTA |
| F0-008 | ALTA | services privados | `userId` é parâmetro do método; chamada interna pode alegar outra identidade | 5 | ABERTA |
| F0-009 | ALTA | segurança por método | somente controller admin usa `@PreAuthorize`; services administrativos não reaplicam role | 3/5 | ABERTA |
| F0-010 | ALTA | `AuthTokenFilter` | cria Authentication sem rejeitar `isEnabled=false`; token anterior ao bloqueio continua válido | 3 | ABERTA |
| F0-011 | ALTA | auth endpoints | sem rate limit para login, registro e refresh | 3/8/9 | ABERTA |
| F0-012 | ALTA | refresh | sem família, reuse detection ou revogação familiar; corrida pode emitir mais de um sucessor | 8 | MITIGAÇÃO PARCIAL |
| F0-013 | ALTA | frontend/backend | paths, DTOs e nomes incompatíveis; URL prod é placeholder | baseline | BLOQUEIO DE BASELINE |
| F0-014 | ALTA | CI/CD | `.github` ausente; sem testes obrigatórios, CodeQL, secret/image/dependency scan ou SBOM | 11 | ABERTA |
| F0-015 | ALTA | backup | nenhum backup/restauração/RPO/RTO documentado ou testado | 13 | ABERTA |
| F0-016 | ALTA | edge frontend | sem CSP/frame-ancestors/referrer/permissions policy no Vercel | 2/9 | ABERTA |
| F0-017 | ALTA | constraints | DB aceita role arbitrária, email com case duplicado e nascimento futuro | 6 | ABERTA |
| F0-018 | MÉDIA | JWT | sem issuer, audience, tipo ou jti; chave local default é inadequada | 3/8 | ABERTA |
| F0-019 | MÉDIA | CORS | mitigação não commitada adiciona allowlist e passou nos probes 200/403, mas profile prod depende de variável obrigatória e o controle pode desaparecer | 3 | MITIGAÇÃO PARCIAL |
| F0-020 | MÉDIA | Actuator | matcher libera `/actuator/**`; habilitação futura torna endpoints públicos automaticamente | 3/9 | ABERTA |
| F0-021 | MÉDIA | Swagger | UI público em produção e JSON retorna 500; duas configs OpenAPI coexistem | 3/9 | ABERTA |
| F0-022 | MÉDIA | contratos | strings de vacinação sem `@Size`, extras não rejeitados, Pageable/sort sem limite | 4 | ABERTA |
| F0-023 | MÉDIA | repositories | schedule/record filtram só por child após validação separada; métodos JPA amplos continuam expostos | 5/6 | ABERTA |
| F0-024 | MÉDIA | observabilidade | sem eventos de segurança, correlation ID, sanitização ou monitoramento | 10 | ABERTA |
| F0-025 | MÉDIA | erros HTTP | recursos não expostos/ausentes podem virar 500 pelo handler genérico; OpenAPI e actuator/env comprovaram | 4/10 | ABERTA |
| F0-026 | MÉDIA | testes | faltam JWT inválido/expirado/tamper, refresh reuse/race, records cruzados, RLS e defesa por camada | 3–12 | ABERTA |
| F0-027 | MÉDIA | profiles de teste | suporte Testcontainers não está importado e `src/test/resources` força localhost:5435 | 0/11 | ABERTA |
| F0-028 | BAIXA | logs | tentativas 401 são registradas em ERROR, facilitando ruído e custo operacional | 10 | ABERTA |
| F0-029 | BAIXA | Docker build | base por tag flutuante, sem `.dockerignore`, `package -DskipTests`, sem healthcheck backend | 9/11 | ABERTA |
| F0-030 | MELHORIA | documentação | README e contrato anterior não refletem integração atual nem estado de segurança | 0+ | ABERTA |
| F0-031 | CRÍTICA | Git/profiles/Docker image | commit concorrente `8e0fa55` versionou segredo JWT/credencial; `application.yml` ativa local por padrão e o JAR os contém. Exige remoção, rotação e análise do histórico | 3/9/11 | ABERTA — BLOQUEIA DEPLOY/IMAGEM |

Prioridade imediata: remover/rotacionar o material de F0-031 e avaliar o histórico antes de qualquer deploy; impedir exfiltração/armazenamento inseguro de tokens; tornar banco privado e sem superuser; estabelecer default deny por rota/método; depois RLS/constraints e pipeline.
