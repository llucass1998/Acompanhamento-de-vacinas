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
- Aspecto transacional preparado em `RlsUserContextAspect`, mas não validado com role sem bypass.

### Bloqueio

Não é seguro declarar a fase aprovada enquanto o datasource de teste/local não usa uma role sem bypass e não há teste A/B com contexto isolado. A próxima ação deve configurar datasource/ownership externamente e adicionar esses testes, sem avançar para Fase 8.

### Commit

`git commit -m "security: prepara roles e row level security"`
