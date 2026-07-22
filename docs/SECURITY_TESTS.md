# Testes de segurança existentes e lacunas

## Baseline executado

| Área | Comando/prova | Resultado |
|---|---|---|
| Backend | `./mvnw.cmd -ntp clean verify` com DB temporário | 38/38 PASSARAM; build passou |
| Migrations | inicialização Flyway em PostgreSQL 18.4 vazio | V1–V7 PASSARAM; aviso de suporte Flyway até PG 17 |
| Frontend install | `npm ci` em worktree isolado | PASSOU, 1212 packages |
| Frontend lint | `npm run lint` após remediação | PASSOU, zero erro |
| Frontend unit | ChromeHeadless | 2/2 PASSARAM |
| Frontend build | `npm run build` | PASSOU com warnings |
| Docker Compose | `docker compose config --quiet` | PASSOU |
| Dockerfile backend | `docker build --pull --no-cache` no HEAD `8e0fa55` | PASSOU em 161,5 s; imagem 133.762.054 bytes |
| Docker runtime isolado | API + PostgreSQL 16.14 sem portas host | `UP`, V1–V7, endpoint privado 401 |
| Docker Compose | config/down/build/up/ps | PASSOU; sem serviço buildável, volume preservado, DB healthy |

## Testes positivos existentes

- Cadastro e login válidos.
- Refresh válido gera novos tokens.
- CRUD de criança própria e soft delete.
- Consulta/registro vacinal válidos.
- USER e ADMIN exercem seus fluxos de campanha previstos.
- Services de calendário, registro, vacinas e campanhas possuem testes unitários básicos.

## Testes negativos existentes

| Ataque/regra | Esperado | Obtido | Camada observada |
|---|---|---|---|
| API protegida sem token | 401 | 401 | Spring Security |
| senha inválida | 401 | 401 | AuthenticationManager/handler |
| USER cria campanha admin | 403 | 403 | rota + controller method security |
| usuário lê/atualiza criança de outro | 404 | 404 | service/repository owner filter |
| nascimento futuro via API | 400 | 400 | Bean Validation |
| aplicação de dose futura | 400 | 400 | Bean Validation |
| registro duplicado no service | erro de negócio | lançado | service; unique também existe |
| campanha com datas invertidas | 422 | 422 | service |
| campanha inválida direto no DB | bloqueio | bloqueada | CHECK |
| criança órfã direto no DB | bloqueio | bloqueada | FK |
| role arbitrária direto no DB | bloqueio desejado | ACEITA | falha de constraint |
| e-mail case-variant direto no DB | bloqueio desejado | ACEITA | falha de constraint |
| nascimento futuro direto no DB | bloqueio desejado | ACEITA | falha de constraint |

## Probes de defesa já demonstrados

- Frontend ignorado/curl sem token: API devolveu 401.
- CORS ausente/curl sem Origin: autenticação continuou obrigatória (401), confirmando que CORS não é auth.
- CORS concorrente: origem permitida recebeu 200/allow-origin, origem maliciosa 403 e request direto sem Origin continuou 401; mudança ainda não commitada.
- Proxy ignorado/acesso direto ao backend local: autenticação continuou obrigatória; rate limit interno não existe.
- Container: processo observado como root, portanto o cenário de redução de impacto falhou.
- Imagem: JAR contém profile local padrão, JWT literal e senha literal; nenhum valor foi impresso e a tag temporária foi removida.
- Repository sem owner versus RLS: não pode ser aprovado; RLS inexiste.

## Casos obrigatórios ainda ausentes

- JWT inválido, expirado, assinatura adulterada, issuer/audience/tipo incorretos.
- Usuário desativado com access token ainda válido.
- Role visual/localStorage adulterada sem alteração de autorização backend.
- Refresh expirado, revogado, reutilizado e duas renovações concorrentes.
- Logout frontend/backend e reutilização posterior.
- Payload com owner/user/role/extra, strings enormes, enum e sort maliciosos.
- Chamada direta de cada service privado com identidade/recurso cruzados.
- Rollback após falha intermediária.
- Corrida de registro de dose comprovando unique.
- Policies RLS: A/B/sem contexto/insert/update/delete/pool.
- Tokens/senhas/headers ausentes de logs e sanitização contra log injection.
- Scan de segredo falso e dependência vulnerável em pipeline.
- Restore de backup e smoke test.

## Cobertura frontend

O HEAD final possui apenas dois testes genéricos; auth, guard, interceptor, storage, concorrência de refresh e services HTTP não são exercidos. Build e TypeScript também não detectaram os paths/DTOs incompatíveis com o backend.

Uma suíte específica de defesa entre camadas será criada somente na Fase 12, após as camadas existirem e os gates anteriores estarem verdes.
