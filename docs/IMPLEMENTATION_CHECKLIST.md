# Checklist de Implementação - Vacina Kids

## FASE 0 — AUDITORIA E PREPARAÇÃO
- [x] Confirmar o repositório e a branch atual.
- [x] Verificar alterações não commitadas.
- [x] Ler o README.
- [x] Ler package.json e angular.json.
- [x] Mapear páginas e models.
- [x] Identificar usos do localStorage e endpoints/rotas.
- [x] Executar testes atuais do frontend.
- [x] Executar lint do frontend.
- [x] Executar build do frontend.
- [x] Criar a branch feat/backend-spring-boot.
- [x] Criar docs/IMPLEMENTATION_CHECKLIST.md, ARCHITECTURE.md, API_CONTRACT.md, DATABASE_MODEL.md.

## FASE 1 — ESTRUTURA INICIAL DO BACKEND
- [x] Criar backend com Maven Wrapper (Java 21, Spring Boot).
- [x] Configurar dependências (Web, JPA, PostgreSQL, Flyway, Validation, Actuator, Testcontainers, Security).
- [x] Criar pacote base.
- [x] Configurar profiles.
- [x] Criar .env.example.
- [x] Atualizar .gitignore.
- [x] Criar docker-compose.yml.
- [x] Configurar PostgreSQL e Flyway.
- [x] Criar primeira migration (V1__create_initial_structure.sql).
- [x] Configurar JPA com ddl-auto=validate.
- [x] Configurar Actuator e healthcheck.
- [x] Configurar CORS inicial (em configs properties).
- [x] Criar teste de carregamento de contexto.
- [x] Criar teste de integração com Testcontainers.
- [x] Validar conexão real com PostgreSQL.
- [x] Validar execução das migrations.
- [x] Validar build.
