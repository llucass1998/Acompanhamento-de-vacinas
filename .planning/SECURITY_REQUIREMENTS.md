# Requisitos de segurança verificáveis

| ID | Requisito | Verificação esperada | Estado Fase 0 |
|---|---|---|---|
| SR-01 | Frontend nunca é autoridade | API sem token retorna 401; role/IDs do cliente não decidem acesso | `PARCIAL` |
| SR-02 | Identidade vem do backend | Principal autenticado usado em toda operação privada | `PARCIAL`: controller extrai, service aceita `userId` arbitrário |
| SR-03 | Isolamento entre usuários | A não lê/altera criança, agenda ou dose de B | `PARCIAL`: há testes de criança; cobertura incompleta |
| SR-04 | Default deny | Somente rotas públicas explicitamente listadas | `PARCIAL`: `anyRequest().authenticated()`, mas matchers amplos de Actuator/Swagger |
| SR-05 | Segurança por método | Falha de rota ainda é bloqueada no método/service | `NÃO ATENDE` fora do controller admin |
| SR-06 | DTO mínimo | DTOs não aceitam owner/user/role/id indevidos | `PARCIAL` |
| SR-07 | Entrada limitada | Tamanho, enum, data, paginação e sort possuem allowlist | `PARCIAL` |
| SR-08 | Propriedade no domínio | Services obtêm principal e validam propriedade | `PARCIAL` |
| SR-09 | Repository privado seguro | Consultas privadas incluem proprietário | `PARCIAL` |
| SR-10 | SQL parametrizado | Sem concatenação de entrada em SQL/JPQL | `ATENDE` no código encontrado |
| SR-11 | Senha protegida | BCrypt e nenhum retorno de `passwordHash` | `ATENDE` |
| SR-12 | Access token restrito | exp, assinatura, issuer, audience, tipo e algoritmo permitidos | `PARCIAL`: assinatura/exp; faltam issuer/audience/type/jti |
| SR-13 | Refresh token protegido | Entropia, hash, rotação, família, reuse detection, revogação | `PARCIAL`: hash e rotação; faltam família/reuse e concorrência |
| SR-14 | Token fora de storage web | Refresh em cookie HttpOnly; access em memória | `NÃO ATENDE` |
| SR-15 | Interceptor em allowlist | Bearer somente para a origem exata da API | `NÃO ATENDE` |
| SR-16 | XSS defensivo | Sem sinks inseguros; CSP/Trusted Types quando aplicável | `PARCIAL`: sem sinks encontrados; sem CSP |
| SR-17 | CORS allowlist | Configuração efetiva e testada | `NÃO ATENDE`: properties não são usadas |
| SR-18 | CSRF analisado | Arquitetura Bearer/cookie coberta por teste | `PARCIAL`: desativado globalmente; sem cookie atual |
| SR-19 | Rate limit | Login, registro, refresh e API possuem limite interno | `NÃO ATENDE` |
| SR-20 | Constraints defensivas | PK/FK/NOT NULL/UNIQUE/CHECK de domínio | `PARCIAL` |
| SR-21 | Menor privilégio PostgreSQL | app não é owner/superuser/BYPASSRLS | `NÃO ATENDE` |
| SR-22 | RLS em dados privados | Contexto ausente nega; pool não vaza | `NÃO ATENDE` |
| SR-23 | Logs seguros | Sem token/senha/cookie; sanitização e correlation ID | `PARCIAL` |
| SR-24 | Auditoria | Eventos de segurança e ações críticas | `NÃO ATENDE` |
| SR-25 | Container não root | UID dedicado, caps reduzidas e filesystem limitado | `NÃO ATENDE` |
| SR-26 | PostgreSQL privado | Sem porta publicada em todas as interfaces | `NÃO ATENDE` |
| SR-27 | Segredos externos | Nenhum segredo real em Git/imagem/config default | `PARCIAL` |
| SR-28 | CI e supply chain | Builds, testes, scanners, SBOM e gates | `NÃO ATENDE` |
| SR-29 | Backup recuperável | Backup criptografado e restauração cronometrada | `NÃO ATENDE` |
| SR-30 | Defesa entre camadas | Cada falha simulada é bloqueada por outra camada | `NÃO ATENDE` |

Nenhum requisito `PARCIAL` deve ser promovido sem teste positivo, negativo e de regressão. Nenhuma decisão do frontend conta como evidência de autorização.
