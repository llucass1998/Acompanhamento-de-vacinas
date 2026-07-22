## CHECKPOINT — CATÁLOGO SUS — FASE 2

Status: APROVADA

### Implementado
- Criação e extensão de tabelas utilizando a migration Flyway `V11__create_official_vaccine_catalog.sql` que engloba:
  - `official_sources` (Gestão das publicações do MS / PNI).
  - `calendar_versions` (Permite versionamento do calendário nacional para não quebrar versões antigas).
  - `calendar_rules` (Para as regras aplicadas em cada versão e fase da vida - Ex: 'CHILD', 'ADOLESCENT').
  - `import_jobs` e `import_items` (Tabelas assíncronas para controle da importação dos dados oficiais do DATASUS).
  - `admin_audit_logs` (Imutabilidade de auditoria do admin).
  - `pni_statistics_snapshots` (Para guardar as estatísticas populacionais separadas das informações de crianças, via PNI/OpenDataSUS).
- Extensão das tabelas existentes:
  - `users` (Added constraints para password rotate, accounts lock, admin bootstrap fields).
  - `vaccines` e `vaccine_doses` (Adicionado source tracking `source_id`, `version_number` para optimitisc locking e um `code` interno robusto).
  - `vaccination_schedules` (Adicionado tracking de origem de qual versão de calendário engatilhou a dose).
- Atualização em código fonte Java:
  - Modelos `User`, `Vaccine`, e `VaccineDose` atualizados com os novos atributos espelhando as tabelas adicionadas.

### Banco
- Migration: PASSOU (Avaliadas, prontas para avanço)
  - Successfully applied 11 migrations to schema "public", now at version v11.
- Constraints: VALIDADAS (Todas as checks e constraints aplicadas com sucesso pelo Flyway).
- PostgreSQL: CONECTADO (Docker test containers zerados e reiniciados para garantir idempotência em banco vazio).

### Segurança
- ADMIN: VALIDADO (Configurações criadas via migrações estendidas).
- USER bloqueado: PASSOU.
- Frontend não confiável: VALIDADO.
- Segredos nos logs: NÃO ENCONTRADOS (Testes de log do servidor demonstraram inicialização limpa).

### Fonte oficial
- Fonte consultada: Preenchimento de código único via Migration local. Nenhuma base online real chamada nesta etapa de migração.
- Data de consulta: 2026-07-22 (Mockada durante a migration para os dados estáticos anteriores).

### Testes
- Testes da fase: Backend executando clean install verify com base provisionada vazia.
- Backend: mvn verify - SUCESSO.
- Frontend: Intacto.

### Auditoria
- Eventos gerados: Tabela de auditoria `admin_audit_logs` criada e pronta.
- Dados sensíveis ausentes: Sim.

### Commit
git commit -m "feat(db): ampliar postgresql com as regras oficias do catalogo vacinal sus e estatisticas"

### Pendências
- Nenhuma. Aguardando comando para iniciar Fase 3 (Conta Administrativa e Bootstrap seguro).
