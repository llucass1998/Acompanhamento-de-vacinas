# Convenções observadas

## Backend

- Organização por feature (`auth`, `child`, `campaign`, etc.) com subpastas controller/service/repository/dto/entity.
- Controllers retornam DTOs; não foi encontrado endpoint retornando entidade JPA diretamente.
- DTOs de entrada são records Java com Bean Validation.
- Services usam `@Transactional`, incluindo `readOnly=true` em leituras.
- Erros usam `ProblemDetail` por `@RestControllerAdvice`.
- Recursos privados costumam retornar 404 para inexistência e acesso cruzado, reduzindo enumeração.
- Repositories usam derived queries e JPQL parametrizado; não foi encontrada concatenação SQL.
- Datas/auditoria técnica são preenchidas por callbacks JPA ou Hibernate timestamps.

Desvios:

- Services privados recebem `userId` em vez de obter a identidade autenticada.
- Somente o controller administrativo tem segurança por método.
- `Pageable` entra sem limite/allowlist de sort.
- Há duas classes de configuração OpenAPI criando beans `OpenAPI`.
- Profiles e testes duplicam `application-test.yml` com estratégias incompatíveis (Testcontainers JDBC versus localhost fixo).

## Frontend

- Componentes standalone e lazy loading.
- Services por domínio usam `HttpClient` e `environment.apiUrl`.
- Formulários usam `FormsModule` e `ngModel`.
- Templates usam interpolação Angular; nenhum `innerHTML`, `DomSanitizer` ou bypass foi encontrado.
- O lint exige `inject()` em vez de injeção por construtor.

Desvios:

- Novos arquivos usam injeção por construtor e quebram o lint.
- Vários services retornam `Observable<any>`.
- Access e refresh token são persistidos em `localStorage`.
- O interceptor considera toda URL absoluta como API.
- Erros de autenticação são enviados a `console.error`.
- Não há testes para auth, guard, interceptor ou services HTTP.
