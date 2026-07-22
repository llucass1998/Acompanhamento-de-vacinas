# Checklist de segurança — estado real

Legenda: `ATENDE`, `PARCIAL`, `NÃO ATENDE`, `NÃO TESTADO`.

| Invariante | Estado | Evidência resumida |
|---|---|---|
| Frontend não é autoridade | PARCIAL | backend exige JWT, mas frontend mantém decisões/storage frágeis |
| Usuário autenticado é obtido no backend | PARCIAL | controller obtém principal; service aceita UUID |
| Owner não vem do JSON | ATENDE | DTOs não contêm owner/userId |
| Usuário não acessa criança de outro | PARCIAL | testes 404 de read/update; faltam todas operações/camadas |
| Usuário não acessa calendário de outro | PARCIAL | service valida criança; teste unitário existe |
| Usuário não registra dose para criança de outro | PARCIAL | regra existe; integração negativa ausente |
| USER não executa admin | PARCIAL | teste 403 e `@PreAuthorize` controller; service desprotegido |
| Entidades JPA não são expostas | ATENDE | controllers usam DTOs |
| DTOs aceitam somente o necessário | PARCIAL | sem owner/role, mas validações/unknown fields incompletas |
| Senhas não ficam em texto puro | ATENDE | BCrypt |
| Tokens não aparecem em logs | PARCIAL | não observados; não há testes de captura/redação |
| Refresh tokens não ficam em texto puro no DB | ATENDE | SHA-256 de token aleatório |
| Backend aplica default deny | PARCIAL | anyRequest autenticado; Actuator/Swagger amplos |
| Services verificam propriedade | PARCIAL | criança validada; identidade ainda é argumento |
| Repositories privados filtram owner | PARCIAL | ChildRepository sim; agendas/records não |
| PostgreSQL tem constraints defensivas | PARCIAL | PK/FK/unique/check campanha; checks importantes faltam |
| Runtime DB tem menor privilégio | NÃO ATENDE | superuser, owner, CREATEDB/CREATEROLE/BYPASSRLS |
| Runtime DB não é owner | NÃO ATENDE | é owner de todas as tabelas |
| Runtime DB não possui BYPASSRLS | NÃO ATENDE | possui |
| Dados privados têm RLS | NÃO ATENDE | zero policies |
| CORS não substitui auth | PARCIAL | mudança não commitada: origem permitida 200, maliciosa 403 e sem Origin 401; prod depende de env |
| AuthGuard não substitui autorização | PARCIAL | API retorna 401; faltam testes frontend/backend coordenados |
| Angular não substitui Bean Validation | PARCIAL | Bean Validation existe, cobertura incompleta |
| Domínio não substitui constraints | PARCIAL | campanha tem ambas; outras regras não |
| Container não root | NÃO ATENDE | uid 0 |
| PostgreSQL não está público | NÃO ATENDE | `0.0.0.0:5435` |
| Segredos não entram no Git/imagem | NÃO ATENDE | commit `8e0fa55` e JAR contêm chave JWT/senha literais; valores não foram exibidos |
| Mudanças de segurança têm testes negativos | NÃO ATENDE | suíte de defesa em profundidade ausente |
| CSP aplicada | NÃO ATENDE | header ausente no edge |
| CI possui scanners e gates | NÃO ATENDE | `.github` ausente |
| Logs/auditoria seguros | NÃO ATENDE | sem auditoria/correlation/redaction testada |
| Backup foi restaurado em teste | NÃO ATENDE | mecanismo ausente |

## Gate da Fase 0

- Backend/migrations: verde no baseline validado.
- Frontend `npm ci`: verde no HEAD final.
- Frontend lint: verde após migração mecânica para `inject()`; nenhuma regra foi desativada.
- Frontend testes: 2 verdes, cobertura insuficiente.
- Frontend build: verde com warnings.
- Docker Compose config: verde.
- Docker build/runtime: build sem cache, API `UP`, V1–V7 e 401 passaram; hardening falhou (root, escrita, sem healthcheck/limites/cap drop/no-new-privileges).
- Ciclo Compose: config/down/build/up/ps passou; volume preservado e DB voltou saudável.

Status da fase: `APROVADA` como auditoria completa. Não iniciar Fase 1 sem `CONTINUAR`.
-
## Fase 1 — modelo de ameaças

- [x] Fronteiras de confiança documentadas.
- [x] Fluxos Mermaid documentados.
- [x] Casos de abuso obrigatórios classificados.
- [x] Riscos críticos com três camadas planejadas.
- [ ] Controles implementados e testados (fases posteriores).
