# Discussão — Fase 7

O PostgreSQL atual usa `vacina_user` como superuser com `BYPASSRLS`, portanto RLS não protege o runtime atual. A fase adiciona roles sem privilégio, habilita policies baseadas em `app.current_user_id` e registra o bloqueio: ainda é necessário migrar o datasource do Spring e definir o contexto em cada transação antes de declarar isolamento efetivo.
