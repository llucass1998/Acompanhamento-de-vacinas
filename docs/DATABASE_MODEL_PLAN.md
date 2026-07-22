# Plano de Auditoria e Modernização do Modelo de Banco de Dados

## Estado Atual
Atualmente, as informações do calendário e vacinas estão em `V3__create_vaccines_and_schedules.sql` e povoados estaticamente em `V4__seed_vaccines.sql`.

Existem as tabelas atuais:
- `users` (id, email, password_hash, name, role, created_at, updated_at)
- `children` (id, user_id, name, birth_date, responsible_name, notes, active)
- `vaccines` (id, name, description, active)
- `vaccine_doses` (id, vaccine_id, dose_name, recommended_age_months, description, active, source, source_version)
- `vaccination_schedules` (id, child_id, vaccine_dose_id, expected_date, applied_date, status, created_at, updated_at)
- `vaccination_records` (historico e aplicação real)

Atualmente `vaccines` e `vaccine_doses` contêm os dados originais. O novo modelo será construído ao redor destas tabelas base para estendê-las de acordo com a restrição do projeto "Não crie um segundo banco de dados. Amplie o PostgreSQL existente".

## Entidades e Tabelas que Precisam Ser Adicionadas / Alteradas

De acordo com o **Catalogo SUS / Administração de PNI**, teremos que criar as seguintes tabelas em novas Flyway migrations:

1. `official_sources` - Rastreio da fonte original (DataSUS/PNI).
2. `calendar_versions` - Versionamento do calendário, permitindo publicação ou rascunho de edições do governo.
3. `calendar_rules` - Regras de idade, restrições e fase da vida associadas às `vaccine_doses` + `calendar_versions`.
4. `import_jobs` e `import_items` - Controle da esteira de importação assíncrona ou por administrador do OpenDataSUS/Arquivos governamentais.
5. `admin_audit_logs` - Tabela de logs imutáveis rastreando quem e quando mudou uma regra vacinal no sistema.
6. `pni_statistics_snapshots` - Cache isolado das doses públicas (estatísticas puras do MS).

### Impacto nas Tabelas Atuais:
- `vaccines`: Adicionar `code` (UK), `display_name`, `prevented_diseases`, `official` e `source_id` (FK), `version_number` (Optimistic Locking).
- `vaccine_doses`: Adicionar `code` (UK com vaccine_id), `dose_order`, `version_number`.
- `vaccination_schedules`: Adicionar vínculos com o versionamento, ou seja, de qual regra/versão essa dose derivou: `calendar_version_id`, `calendar_rule_id`, `source_id`.
- `users`: Adicionar controle seguro de admin (`must_change_password`, `last_password_change_at`, `failed_login_attempts`, `locked_until`).

## Segurança e Controle
A implementação da conta administrativa será feita através de propriedades do `application.yml` (`BOOTSTRAP_ADMIN_ENABLED`, etc.) sem senhas expostas, para criar dinamicamente o primeiro ADMIN em ambiente vazio, usando `BCrypt`.

O Spring Security bloqueará ativamente o acesso `/api/v1/admin/**` exigindo a role ADMIN (não confiando no frontend em nenhum aspecto, como a instrução pede de forma absoluta).

## Próximos Passos
- Concluir Fase 1 enviando a aprovação.
- Na Fase 2, escrever as instruções SQL do Flyway (como `V10__create_official_vaccine_catalog.sql` em diante).
