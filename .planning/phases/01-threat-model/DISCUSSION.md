# Discussão — Fase 1: Modelo de ameaças

## Estado de entrada

- Fase 0 aprovada como auditoria, com 31 achados abertos.
- O frontend, navegador, storage, JSON, URLs, headers, uploads e APIs externas são não confiáveis.
- Deploy permanece bloqueado por segredo literal/profile local empacotado no JAR (F0-031).
- Esta fase é documental: não altera código, migrations, Docker ou CI.

## Riscos prioritários

1. Acesso cruzado entre usuários a crianças, agendas, doses e comprovantes.
2. Escalonamento de privilégio por role adulterada ou endpoint administrativo exposto.
3. Roubo/reutilização de tokens e ausência de revogação/rotação.
4. Consulta privada sem owner filter e ausência de RLS.
5. Segredos versionados e cadeia de suprimentos sem gates.
6. XSS, abuso de payload, brute force e operações concorrentes.

## Arquivos e evidências envolvidos

- `frontend/src/app`: guards, interceptor, services e telas.
- `backend/src/main/java`: SecurityConfig, controllers, services, repositories e entidades.
- `backend/src/main/resources/db/migration`: V1–V7.
- `docker-compose.yml`, `backend/Dockerfile`, `.github` (quando presente) e configurações Spring.
- Evidências consolidadas em `.planning/codebase/*` e `docs/SECURITY_TESTS.md`.

## Critério da discussão

Todo risco crítico deve possuir pelo menos três controles independentes, sendo ao menos um preventivo e um de detecção. A ausência atual de controles é registrada como risco residual, não presumida como mitigada.
