# Plano — Fase 7

- Criar roles `vacina_app` e `vacina_migrator` sem superuser/BYPASSRLS.
- Habilitar RLS em tabelas privadas.
- Policies usam somente contexto local `app.current_user_id`.
- Verificar schema e registrar o bloqueio do datasource atual.
