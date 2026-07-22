# Relatório de auditoria — Fase 0

## Resumo executivo

O sistema possui uma base backend funcional, DTOs, transações, alguns filtros de propriedade, hash de senha, hash/rotação simples de refresh e constraints úteis. Entretanto, ainda não implementa defesa em profundidade: frontend e backend estão incompatíveis, tokens web ficam em `localStorage`, o interceptor pode vazar Bearer para qualquer origem HTTP, o banco runtime é superuser e não há RLS, containers rodam como root e não existe CI de segurança.

O baseline final está vermelho por 16 erros de lint. A Fase 0 é, portanto, reprovada e a Fase 1 não pode começar.

## Cobertura do checklist

- [x] Repositório, branch, status e mudanças concorrentes identificados.
- [x] Frontend e backend confirmados no mesmo monorepo/branch.
- [x] README, package, Angular, POM, profiles, Dockerfiles, Compose e Vercel lidos.
- [x] Rotas, guard, interceptor, storage e sinks XSS mapeados.
- [x] SecurityFilterChain, filtro JWT, refresh e segurança por método mapeados.
- [x] Propriedade, `findById`, `userId`, queries customizadas e repositories mapeados.
- [x] Migrations, constraints, owners, roles, grants implícitos e RLS inspecionados.
- [x] Logs, uploads, Actuator, Swagger, secrets/defaults e dependências inspecionados.
- [x] Testes/backend/build/migrations executados em banco temporário.
- [x] Frontend final validado em worktree isolado.
- [x] Dockerfile construído e runtime inspecionado enquanto daemon estava disponível.
- [ ] Baseline completamente verde: lint frontend falhou.

## Controles presentes

- Spring Security stateless com `anyRequest().authenticated()`.
- Endpoints admin por rota e `@PreAuthorize` no controller.
- JWT HMAC com expiração e role recarregada do banco.
- BCrypt para senha.
- Refresh opaco aleatório; somente SHA-256 persistido; rotação simples.
- DTOs em vez de entidades e Bean Validation para vários campos.
- ChildRepository filtra por usuário; services validam acesso antes de agenda/registro.
- Transações nos fluxos de escrita.
- PK/FK/NOT NULL, unique por dose e check de datas de campanha.
- Erro 500 não expõe stack ao cliente.
- Dockerfile multi-stage e JRE final.
- HTTPS/HSTS observados no frontend publicado.

## Falhas arquiteturais principais

1. Não há segunda barreira independente após o service: banco sem RLS e role superuser.
2. Services não obtêm a identidade do contexto; confiam no parâmetro interno `userId`.
3. Segurança por método não cobre domínio/admin service.
4. Frontend armazena tokens e envia Bearer fora de uma allowlist de origem.
5. Refresh não possui família/reuse detection e o logout frontend não revoga servidor.
6. Contratos frontend/backend não integram e produção usa host placeholder.
7. Containers/rede não aplicam menor privilégio.
8. CI, scanners, auditoria, monitoramento e backup não existem.

## Riscos e prioridade

O registro completo está em `.planning/codebase/CONCERNS.md`. Prioridade:

1. Repor baseline verde e estabilizar integração.
2. Conter tokens no frontend e limitar interceptor.
3. Endurecer autenticação/autorização por rota, método e domínio.
4. Separar roles PostgreSQL, fechar porta pública e aplicar RLS.
5. Adicionar constraints defensivas e testes de concorrência/acesso cruzado.
6. Endurecer Docker/edge, CI/CD e observabilidade.
7. Testar defesa entre camadas e recuperação.

## Conclusão da fase

O mapa é suficiente para planejar hardening, mas o gate de qualidade não está verde. A auditoria não declara o sistema seguro e não autoriza a Fase 1.
