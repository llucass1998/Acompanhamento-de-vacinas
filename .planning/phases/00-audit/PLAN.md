# Plano executado — Fase 0

| Tarefa | Objetivo | Arquivos/áreas | Alteração esperada | Risco | Teste positivo | Teste negativo | Regressão | Critério | Commit esperado |
|---|---|---|---|---|---|---|---|---|---|
| T0.1 | Confirmar escopo Git/GSD | raiz, Git, PATH | nenhuma | mapear branch errada | `rev-parse`, branch e status | detectar dirty/conflito | N/A | raiz/branch registrados | docs phase 0 |
| T0.2 | Mapear frontend | `src`, Angular, Vercel | nenhuma | tratar guard como segurança | inventário/rotas/services | buscar storage e sinks XSS | lint/test/build | fluxo e lacunas registrados | docs phase 0 |
| T0.3 | Mapear backend | Java, Security, API | nenhuma | omitir endpoint/owner check | inventário completo | buscar `findById`, IDs, roles, logs, uploads | `clean verify` | auth/authz descritos | docs phase 0 |
| T0.4 | Mapear PostgreSQL | migrations e metadados | schema temporário somente | tocar dados reais | V1–V7 em vazio | role/status/email/data inválidos | JPA validate | owners/RLS/constraints provados | docs phase 0 |
| T0.5 | Mapear infraestrutura | Docker/Compose/edge | imagem/container temporários | interferir containers | build/runtime/health | UID root, porta DB, CORS | compose config | superfície registrada | docs phase 0 |
| T0.6 | Auditar supply chain | npm/Maven/Git/CI | nenhuma | scanner indisponível | árvores inventariadas | audit/secrets/tools ausentes | builds | achados classificados | docs phase 0 |
| T0.7 | Verificar baseline | suites existentes | artefatos ignorados | falso verde por mudança concorrente | testes/builds | 401/403/cross-access/constraints | backend/frontend | todos os resultados registrados | docs phase 0 |
| T0.8 | Registrar estado | `.planning`, `docs` | somente documentação | declarar segurança sem prova | revisão cruzada | marcar lacunas abertas | status Git | check-in fiel | `docs: mapeia arquitetura e seguranca atual` |

## Resultado do plano

T0.1–T0.8 foram executadas. O critério de aprovação global não foi satisfeito porque `npm run lint` falhou no HEAD final. Nenhuma tarefa de correção foi adicionada fora do plano.
