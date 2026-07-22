# Remediação do gate — Fase 0

## Discussão

A auditoria terminou em `REPROVADA` porque o frontend integrado no commit `12fe3da` introduziu 16 violações de `@angular-eslint/prefer-inject`. O comando `CONTINUAR` reabre exclusivamente o gate da Fase 0; a Fase 1 permanece bloqueada.

O escopo desta remediação é mecânico: substituir injeção por parâmetros de construtor por `inject()` nos dez arquivos apontados pelo lint. Não serão alterados fluxos de autenticação, armazenamento de tokens, contratos HTTP, regras de autorização, migrations, Docker ou dependências. Os arquivos `backend/src/main/resources/application.yml` e `application-local.yml` possuem mudanças concorrentes e serão preservados fora do commit.

Riscos identificados:

- alterar acidentalmente o comportamento dos serviços ou componentes ao remover construtores;
- interferir no `ng serve` ativo ou em seu `node_modules` bloqueado;
- considerar verde um teste executado sobre dependências incompletas;
- validar o backend contra o banco de desenvolvimento do usuário;
- ocultar indisponibilidade do Docker ou mudanças concorrentes posteriores.

Mitigações: mudanças estritamente mecânicas, revisão do diff, verificação frontend em worktree isolado com `npm ci`, banco PostgreSQL descartável para backend/Flyway e nova conferência do Git antes do commit.

## Plano

| Tarefa | Objetivo | Arquivos envolvidos | Alteração esperada | Risco | Teste positivo | Teste negativo | Regressão | Critério de conclusão | Commit esperado |
|---|---|---|---|---|---|---|---|---|---|
| T0.R1 | Restaurar o lint | dez arquivos TypeScript apontados pelo ESLint | usar `inject()` sem mudar a lógica | regressão de DI | `npm run lint` | nenhuma regra desativada/supressão adicionada | testes e build frontend | zero erro de lint e diff mecânico | `chore: restaura baseline da fase 0` |
| T0.R2 | Validar frontend limpo | `package-lock.json`, fontes e specs existentes | nenhuma | dependências locais incompletas | `npm ci`, lint, testes e build | confirmar ausência de bypass do lint | suíte frontend | quatro comandos verdes em worktree isolado | mesmo commit |
| T0.R3 | Revalidar backend e banco | backend e V1–V7 | nenhuma | tocar banco existente | `clean verify` e Flyway em banco vazio | datasource de desenvolvimento não utilizado | 38 testes existentes | build e migrations verdes | mesmo commit |
| T0.R4 | Revalidar Docker | Compose e Dockerfile existentes | nenhuma | daemon indisponível | `docker compose config` e build | confirmar processo root já auditado | smoke test aplicável | resultados reais registrados | mesmo commit |
| T0.R5 | Fechar o checkpoint | `STATE.md`, `ROADMAP.md`, check-in e evidências | atualizar estado real | aprovação sem evidência | revisão dos relatórios | gates vermelhos mantêm reprovação | `git status` | check-in consistente e alterações concorrentes excluídas | mesmo commit |

## Critério de parada

Se lint, testes, build, migrations ou Docker obrigatório falharem, a Fase 0 continuará `REPROVADA`. Mesmo se aprovada, o trabalho para após o check-in e aguarda novo `CONTINUAR` antes da Fase 1.

## Resultado executado

- [x] T0.R1 — 16 violações corrigidas com `inject()`; nenhuma supressão adicionada.
- [x] T0.R2 — `npm ci`, lint, 2 testes ChromeHeadless e build passaram em worktree isolado.
- [x] T0.R3 — 38 testes backend, JAR e V1–V7 passaram em PostgreSQL 18.4 descartável.
- [x] T0.R4 — Compose, build sem cache, runtime isolado, migrations, health e 401 foram revalidados; hardening ausente foi registrado.
- [x] T0.R5 — estado, roadmap, checklist, testes, evidências e check-in atualizados.

Resultado global: `APROVADA` como auditoria. PostgreSQL, worktrees, containers, rede, tag e diretórios temporários foram removidos após a coleta de evidências. A aprovação não corrige nem aceita os achados de segurança.
