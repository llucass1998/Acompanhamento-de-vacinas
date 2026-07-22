# Mapa de segurança atual por camada

| Camada | Controles existentes | Lacunas independentes |
|---|---|---|
| Frontend | AuthGuard de UX; interpolação Angular; input password; logout visual | tokens em localStorage; Bearer para qualquer HTTP; sem refresh/revogação; sem CSP; contrato inválido |
| Edge/Vercel | HTTPS e HSTS observados; SPA rewrite | sem CSP, Permissions-Policy, Referrer-Policy ou frame-ancestors; `Server: Vercel`; ACAO `*` |
| Spring Security | stateless; BCrypt; JWT assinado; rota admin por role; anyRequest autenticado; CORS allowlist em mudança local testada | CORS não commitado/profile prod depende de env; Actuator/Swagger amplos; sem rate limit; CSRF global off; sem issuer/audience/type; usuário inativo pode manter access token |
| Controllers/DTOs | `@Valid`; DTOs não aceitam owner/user/role; ProblemDetail sem stack | campos extras não são explicitamente rejeitados; limites incompletos; paginação/sort livres |
| Services/domínio | transações; validação de propriedade da criança; duplicidade lógica | identidade é parâmetro; admin service sem método protegido; refresh concorrente; invariantes incompletas |
| Repositories | parâmetros/derived queries; ChildRepository filtra user | repositories privados herdaram métodos amplos; agendas/records não incluem owner; sem defesa contra service incorreto |
| PostgreSQL | PK, FK, NOT NULL, uniques de dose e check de campanha | runtime superuser/owner/BYPASSRLS; sem RLS; checks de role/status/datas faltantes; email case-sensitive |
| Docker/rede | multi-stage; JRE final; volume/healthcheck do Postgres | root, filesystem gravável, sem caps/limits/health do backend; Postgres em `0.0.0.0:5435`; sem stack interna completa |
| CI/CD | package lock e Maven wrapper scripts | nenhum workflow, scan, SBOM, Dependabot, secret scan ou gate |
| Logs/monitoramento | respostas 500 não expõem stack; tokens não foram encontrados em logs | sem correlation/auditoria; 401 em ERROR; sem sanitização/alertas; logs SQL em local/test |
| Backup/IR | volume persistente | sem backup, restore test, RPO/RTO ou runbook |

## Superfície exposta

- Públicos: register, login, refresh, todos paths do Actuator permitidos pela cadeia, Swagger UI e OpenAPI.
- Produção expõe somente health por configuração do Actuator, mas `/actuator/**` ficará público se outro endpoint for habilitado.
- Swagger UI respondeu 200; `/v3/api-docs` respondeu 500 no teste de runtime.
- Todo endpoint não explicitamente público exige autenticação, inclusive campanhas e vacinas.
- Nenhum upload/controller de arquivo foi encontrado; `proof_url` existe apenas no modelo/banco.

## Segredos

- Nenhum `.env` real foi encontrado no histórico; `.env.example` está versionado.
- Existem senhas/chaves determinísticas de desenvolvimento e teste em YAML, Compose e scripts soltos.
- O JAR final inclui todos os `application-*.yml`, inclusive profiles local/test.
- O commit concorrente `8e0fa55` versionou senha local e chave JWT; o build confirmou ambas dentro do JAR. F0-031 bloqueia deploy/push e exige rotação, remoção e avaliação do histórico.
