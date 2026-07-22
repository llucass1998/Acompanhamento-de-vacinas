# Discussão — Fase 3: Spring Security

Estado: Fase 2 aprovada. A configuração já era stateless e tinha `@EnableMethodSecurity`, porém expunha todo Actuator e Swagger, aceitava JWT sem marcador de tipo e exigia CORS sem fallback no profile de testes.

Risco crítico: o frontend pode ser removido; somente a cadeia de filtros, claims assinadas, regras de rota e método devem decidir acesso.

Decisões: manter CSRF desabilitado somente porque a API usa bearer stateless nesta fase; restringir Actuator a health; exigir claim `typ=access`; manter endpoints de autenticação públicos; manter CORS allowlist configurável com default apenas para testes locais.
