# CHECK-IN GSD DE SEGURANÇA — FASE 6

Status: APROVADA

### Controles

- Migration V8 adiciona CHECK para roles, status de agenda, idade não negativa e campos textuais.
- UNIQUE `(child_id, dose_id)` já protege duplicidade de agenda e registro.
- Foreign keys existentes preservam integridade referencial.
- DTO de observações alinhado ao limite SQL de 500 caracteres.

### Testes

- `./mvnw.cmd -ntp test`: PASSOU após migration aplicada no PostgreSQL de teste.
- Migrations V1–V8: PASSARAM.
- Testes diretos de violação SQL: planejados para Fase 7/12 junto ao banco isolado.

### Vulnerabilidades

Nenhuma nova. F0-031 permanece aberto.

### Commit

`git commit -m "security: adiciona integridade defensiva no postgresql"`

### Próxima fase

Fase 7 — Menor privilégio e RLS — aguarda novo `CONTINUAR`.
