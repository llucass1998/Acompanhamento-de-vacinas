# Checkpoint Fase 6 - Motor de Agendamento (Calendar Rules)

## O que foi implementado
1. **Entidades**:
   - Atualizada a entidade `CalendarRule` para incluir `source_id` obrigatório.
2. **Services**:
   - `CalendarRuleService` com criação de regras amarradas à versão do calendário (`CalendarVersion`), respeitando o `source_id`.
   - Adicionado registro de auditoria (`AdminAuditLog`) para operações de criação de regra.
3. **Controladores Administrativos**:
   - `AdminCalendarRuleController` mapeando rotas para criação e listagem de regras.
   - Segurança garantida com `@PreAuthorize("hasRole('ADMIN')")` e extração segura do `adminId` via autenticação do Spring Security.
4. **Testes de Integração**:
   - Configurado `AdminCalendarRuleControllerIntegrationTest` com limpeza de banco resiliente (usando `JdbcTemplate` para contornar chaves estrangeiras entre `admin_audit_logs`, `calendar_rules`, `calendar_versions`, `vaccine_doses`, e `vaccines`).
   - Teste validando a criação de regras em API autenticada retornando status `201 Created`.

## Próximos Passos
Avançar para a **Fase 7**, seguindo rigorosamente as restrições arquiteturais e de segurança definidas.