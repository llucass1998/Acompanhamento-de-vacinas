# Segurança do PostgreSQL — estado atual

## Topologia

O Compose cria `postgres:16-alpine`, banco `vacina_kids`, role definido por `POSTGRES_USER` e volume persistente. A porta `5432` do container é publicada como `5435` em todas as interfaces. Backend não está no Compose e conecta pelo host/porta.

## Roles e privilégios

No banco temporário criado com a mesma estratégia do Compose, o role apresentou:

| Atributo | Estado |
|---|---|
| LOGIN | sim |
| SUPERUSER | sim |
| CREATEDB | sim |
| CREATEROLE | sim |
| BYPASSRLS | sim |
| owner das tabelas | sim |

Não existem `vacina_owner`, `vacina_migrator` e `vacina_app`. O mesmo usuário executa migrations e queries runtime. Isso viola menor privilégio e elimina contenção após comprometimento da aplicação.

## RLS

- `children`: desabilitado.
- `vaccination_schedules`: desabilitado.
- `vaccination_records`: desabilitado.
- `refresh_tokens`: desabilitado.
- Demais tabelas: desabilitado.
- `pg_policies`: zero linhas.

Não há `app.current_user_id`, `SET LOCAL`, `ENABLE/FORCE ROW LEVEL SECURITY`, `USING` ou `WITH CHECK`. Portanto, ausência de contexto não nega dados e uma consulta repository sem owner não tem proteção SQL.

## Migrations

Flyway V1–V7 foram aplicadas com sucesso em banco PostgreSQL 16.14 vazio. O schema final contém:

- `users`, `refresh_tokens`, `children`.
- `vaccines`, `vaccine_doses`.
- `vaccination_schedules`, `vaccination_records`.
- `campaigns`.

JPA `ddl-auto=validate` iniciou após as migrations.

## Constraints atuais

Presentes:

- PKs UUID em todas as tabelas.
- FKs entre usuário/criança/token e vacina/dose/agenda/registro.
- `NOT NULL` nos campos centrais.
- unique de `users.email` e `refresh_tokens.token_hash`.
- unique `(child_id, dose_id)` em agendas e registros.
- `CHECK (end_date >= start_date)` em campanhas.
- índices em owner/child, datas e active onde migrations definem.

Ausentes ou inadequadas:

- email é único apenas de forma case-sensitive; normalização depende do backend.
- `users.role` não tem CHECK.
- `vaccination_schedules.status` não tem CHECK.
- nascimento/aplicação futura não são limitados no banco.
- `recommended_age_months` não tem CHECK de não negativo.
- migration não cria unique de `vaccines.name`, apesar da entidade JPA declarar unique.
- algumas nulabilidades/tamanhos de entidade e migration não coincidem.

## Evidências negativas diretas

Todos os inserts de prova foram executados em transações revertidas:

- `role='ROOT'`: aceito.
- dois emails iguais sob `lower(email)`: aceitos.
- criança com nascimento em 2999: aceita.
- campanha invertida: bloqueada por CHECK.
- child com user inexistente: bloqueado por FK.

## Direção futura

1. Criar owner/migrator/runtime separados por mecanismo operacional seguro.
2. Tornar runtime `NOSUPERUSER NOCREATEDB NOCREATEROLE NOBYPASSRLS` e não owner.
3. Remover publicação pública do Postgres e usar rede interna.
4. Adicionar constraints em migrations incrementais, após limpeza/verificação de dados existentes.
5. Implantar RLS com contexto transacional originado do Authentication.
6. Testar contexto ausente, A/B, escrita cruzada e vazamento de pool.

Nenhuma dessas alterações foi executada na Fase 0.
