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

### Evidência adicional

- Teste SQL A/B com `vacina_app`: usuário A viu 1 linha própria e 0 de B; usuário B viu 1 própria; sem contexto viu 0.
- INSERT sem contexto foi bloqueado por RLS.
- A suíte completa usando `vacina_app` ainda falha em fixtures que persistem diretamente sem contexto.

### Bloqueio

Não é seguro declarar a fase aprovada enquanto o datasource de teste/local não usa uma role sem bypass e não há teste A/B com contexto isolado. A próxima ação deve configurar datasource/ownership externamente e adicionar esses testes, sem avançar para Fase 8.

### Commit

`git commit -m "security: prepara roles e row level security"`
