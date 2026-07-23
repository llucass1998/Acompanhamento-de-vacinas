# Checkpoint Fase 7 - Geração Automática de Agendamentos (Vaccination Schedules)

## O que foi implementado
1. **Modelos**:
   - `VaccinationSchedule` estendido com as referências `calendarVersion`, `calendarRule`, `source` (OfficialSource) e `generatedAt`, conforme migração V11.
2. **Consultas JPA**:
   - `CalendarVersionRepository.findFirstByStatusOrderByValidFromDesc("PUBLISHED")` para encontrar o calendário oficial ativo.
   - `CalendarRuleRepository.findAllByCalendarVersionIdAndActiveTrue` para carregar as regras.
3. **Serviços (Integração do Motor)**:
   - `VaccinationScheduleService.generateScheduleForChild` refatorado para ler e aplicar iterativamente as regras do `CalendarRule` em vez de apenas buscar doses ativas indiscriminadamente.
   - Cálculo dinâmico das datas (`expectedDate`) baseado em `recommendedAgeMonths` ou `recommendedAgeDays` presentes na regra.
   - Fallback de segurança para legados caso nenhum calendário oficial esteja publicado.
   - Atualização no `ChildService` injetando `VaccinationScheduleService` para gerar as doses no ato de criação da criança.
4. **Testes**:
   - `ChildControllerIntegrationTest` adaptado para refletir as tabelas recém-criadas na limpeza do banco (usando `JdbcTemplate`). Todos os 8 testes integrados operando perfeitamente (`201 Created` e processamento no backend funcionando liso).

## Próximos Passos
Avançar para a **Fase 8** (Importação de Dados Open Data API / Jobs), gerenciando registros de carga em massa com `import_jobs` e `import_items`.