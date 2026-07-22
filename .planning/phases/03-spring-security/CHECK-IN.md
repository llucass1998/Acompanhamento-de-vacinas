# CHECK-IN GSD DE SEGURANÇA — FASE 3

Status: APROVADA

### Objetivo da fase

Aplicar autenticação e autorização independentes do frontend com menor superfície pública.

### Tarefas planejadas

- [x] Restringir Actuator a health.
- [x] Remover Swagger público da cadeia padrão.
- [x] Marcar access token com `typ=access` e validar o claim.
- [x] Manter default deny para rotas não explicitamente públicas.
- [x] Revalidar testes backend.

### Controles implementados

- Rotas privadas exigem autenticação.
- Rotas `/api/v1/admin/**` exigem `ROLE_ADMIN`.
- JWT sem assinatura válida, expirado, sem subject/email ou sem `typ=access` é rejeitado.
- Actuator público limitado a health.
- CORS mantém allowlist e fallback somente para ambientes sem propriedade.

### Testes positivos

- `./mvnw.cmd -ntp test`: 38 testes PASSARAM.
- Flyway validou schema existente e aplicação subiu no profile test.

### Testes negativos

- Refresh token usado como access JWT: rejeitado pela ausência de `typ=access`.
- Endpoint não explicitamente público: protegido por `anyRequest().authenticated()`.
- Swagger e Actuator não-health: não liberados pela cadeia.

### Build

- Backend: PASSOU via testes Maven.
- Frontend/Docker: não aplicável nesta fase.

### Banco

- Migrations: validadas durante os testes.
- RLS: pendente para Fase 7.

### Vulnerabilidades encontradas

Nenhuma nova. F0-031 e demais achados continuam abertos.

### Commit

`git commit -m "security: aplica default deny e valida access jwt"`

### Estado do GSD

Documentação da fase arquivada; alterações concorrentes preservadas.

### Próxima fase

Fase 4 — Controllers e DTOs — aguarda novo `CONTINUAR`.
