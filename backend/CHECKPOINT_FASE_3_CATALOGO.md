## CHECKPOINT — CATÁLOGO SUS — FASE 3

Status: APROVADA

### Implementado
- Bootstrap de Admin seguro (`AdminBootstrapService`) configurável por variáveis de ambiente (`BOOTSTRAP_ADMIN_ENABLED`, `BOOTSTRAP_ADMIN_EMAIL`, etc.), garantindo a primeira conta administrativa.
- Obrigação lógica de troca de senhas no primeiro acesso: A conta Bootstrap ou qualquer nova conta é salva com a flag `mustChangePassword = true` associado ao `@PreAuthorize`. A rota `/api/v1/admin/auth/change-password` foi disponibilizada.
- `AuditService` e `AdminAuditLog` configurados para registrar as interações (já iniciando pela troca de senha de boot).
- Endpoints administrativos assegurados pelo Spring Security (`/api/v1/admin/**`) negando por padrão acessos convencionais e bloqueando explicitamente usuários sem `Role.ADMIN`. O guard confia exclusivamente na role fornecida pelo token JWT gerado e lido no banco.

### Banco
- Migration: Nenhuma extra. A base já contempla as colunas.
- PostgreSQL: CONECTADO e íntegro.

### Segurança
- ADMIN: VALIDADO. Somente através do token assinado o Spring permite o acesso.
- USER bloqueado: PASSOU.
- Frontend não confiável: VALIDADO. Apenas a API toma a decisão (o Frontend poderá criar o AdminGuard só como reflexo visual, nunca como controle de rota isolada).
- Segredos nos logs: NÃO ENCONTRADOS.

### Testes
- Backend Build: PASSOU (`mvn clean verify`).
- Servidor Spring: Iniciou saudável (`UP`). 

### Auditoria
- Eventos gerados: Tabela pronta e Serviço transacional com propagação assíncrona (`REQUIRES_NEW`) gerado.
- Dados sensíveis ausentes: Sim (Apenas ids de transação, data antiga, data nova e ação).

### Commit
git commit -m "feat(auth): conta administrativa segura com bootstrap, reset de senha forcado e servico de auditoria"

### Pendências
- Nenhuma. Aguardando a aprovação para iniciar a **Fase 4 (Catálogo administrativo base - Services/Controllers)**.
