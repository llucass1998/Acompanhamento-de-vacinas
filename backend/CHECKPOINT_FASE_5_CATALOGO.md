# CHECKPOINT: FASE 5 — Versões do Calendário e Regras de Vacinação

## Realizado
- Criadas as entidades `CalendarVersion` e `CalendarRule` de forma totalmente acoplada com as versões do PNI e rastreadas pelas fontes (`OfficialSource`).
- Atualizadas as migrações via Flyway para acomodar o constraint faltante `notes` na tabela `calendar_rules` via migração `V12__add_notes_to_calendar_rules.sql`.
- Adicionados os repositórios `CalendarVersionRepository` e `CalendarRuleRepository`.
- Implementados `CalendarVersionRequest` e `CalendarVersionResponse` (DTOs), e `CalendarVersionService` integrado com `AuditService`.
- Exposto endpoint administrativo `/api/v1/admin/calendar-versions` (Controller REST).
- Desenvolvido e validado com sucesso o teste de integração `AdminCalendarVersionControllerIntegrationTest`.

## Próximo Passo
- Iniciar a Fase 6 (Motor de Agendamento baseado em Regras) ou concluir as interfaces de `CalendarRule` (serviços e controllers equivalentes aos do CalendarVersion) dependendo do andamento solicitado.
