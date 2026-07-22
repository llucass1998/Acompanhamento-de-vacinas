# Plano — Fase 3

| Tarefa | Arquivos | Verificação |
|---|---|---|
| Default deny e superfície pública | `SecurityConfig` | actuator/Swagger não públicos; rotas privadas autenticadas |
| Validação de access JWT | `JwtUtils` | claim `typ=access`, assinatura, expiração e subject |
| Compatibilidade de configuração | `SecurityConfig` | profile test sobe sem segredo de CORS |
| Regressão | backend | 38 testes e build Maven |
