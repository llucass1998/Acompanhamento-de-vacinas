# CHECK-IN GSD DE SEGURANÇA — FASE 7

Status: REPROVADA

### Controles implementados

- Migration V9 cria roles sem superuser/BYPASSRLS.
- RLS e policies de proprietário foram criadas para children, schedules, records e refresh_tokens.
- Contexto ausente resulta em nenhuma correspondência para roles não privilegiadas.

### Testes

- Migrations V1–V9 e suíte backend: PASSARAM.
- RLS efetivo no runtime: FALHOU como gate, pois `vacina_user` atual é superuser/BYPASSRLS.
- Contexto transacional Spring: PENDENTE.

### Bloqueio

Não é seguro declarar a fase aprovada enquanto o backend usa usuário superuser e não executa `SET LOCAL app.current_user_id` por transação. A próxima ação deve migrar datasource/ownership e adicionar testes isolados de A/B, sem avançar para Fase 8.

### Commit

`git commit -m "security: prepara roles e row level security"`
