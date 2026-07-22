# Roadmap GSD de hardening

Cada fase termina em check-in, commit atômico e parada. A próxima fase exige `CONTINUAR`, mas nenhum avanço é permitido com gates vermelhos.

| Fase | Objetivo | Gate principal | Estado |
|---|---|---|---|
| 0 | Mapa do código e auditoria | mapa, migrations limpas, testes/build/lint verdes | `APROVADA`: auditoria completa, 31 achados abertos |
| 1 | Modelo de ameaças | casos de abuso, fronteiras e três controles para riscos críticos | `AGUARDA CONTINUAR` |
| 2 | Segurança Angular/Ionic | storage, interceptor, XSS e headers com testes | `NÃO INICIADA` |
| 3 | Spring Security | default deny, JWT e segurança por método | `NÃO INICIADA` |
| 4 | Controllers e DTOs | contratos mínimos, limites e rejeição negativa | `NÃO INICIADA` |
| 5 | Services e domínio | identidade interna, propriedade, transação e concorrência | `NÃO INICIADA` |
| 6 | Repositories e constraints | consultas por owner e integridade defensiva | `NÃO INICIADA` |
| 7 | Menor privilégio e RLS | roles separadas e isolamento SQL comprovado | `NÃO INICIADA` |
| 8 | Tokens, cookies e sessões | cookie web, família, rotação e reuse detection | `NÃO INICIADA` |
| 9 | Edge, Docker e rede | HTTPS/headers/rate limit e containers não root | `NÃO INICIADA` |
| 10 | Logs, auditoria e monitoramento | eventos sem vazamento | `NÃO INICIADA` |
| 11 | DevSecOps | CI, scans, SBOM e gates | `NÃO INICIADA` |
| 12 | Testes entre camadas | matriz de defesa em profundidade executável | `NÃO INICIADA` |
| 13 | Backup e incidentes | restore testado, RPO/RTO e resposta | `NÃO INICIADA` |

## Critério para reabrir o gate da Fase 0

1. [x] Resolver os 16 erros de lint sem desativar regras.
2. [x] Reexecutar `npm ci`, lint, testes e build em worktree isolado do `ng serve` concorrente.
3. [x] Reexecutar `./mvnw clean verify` e migrations em banco vazio com overrides que isolam as configurações concorrentes.
4. [x] Confirmar build, runtime e ciclo Compose quando o daemon estiver disponível.
5. [x] Atualizar `STATE.md` e o check-in com evidências novas.

Commit documental esperado para a auditoria: `docs: mapeia arquitetura e seguranca atual`.
