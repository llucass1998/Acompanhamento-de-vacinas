# Contrato de API (Swagger / OpenAPI 3)

A documentação interativa da API está disponível via Swagger UI e os artefatos seguem o padrão OpenAPI 3.

## Endpoints de Documentação
Após iniciar o servidor (`./mvnw spring-boot:run` ou rodar a aplicação Spring Boot), as interfaces de documentação estarão disponíveis em:
- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) (Interface amigável para visualizar e testar os endpoints)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) (Especificação em formato JSON)

## Autenticação
A API utiliza o método **Bearer Token (JWT)**. Para acessar os endpoints protegidos:
1. Faça login via `/api/v1/auth/login`.
2. O sistema retornará um `accessToken`.
3. Para testar via Swagger UI, clique no botão **Authorize**, cole o `accessToken` e confirme. Todas as chamadas subsequentes utilizarão este token.

## Principais Recursos e Códigos de Status

### Autenticação (`/api/v1/auth`)
- `POST /register`: Criação de novo usuário (Permite `ROLE_USER` ou `ROLE_ADMIN`).
- `POST /login`: Autenticação e retorno de tokens JWT (Access e Refresh).
- `POST /refresh`: Renovação do Access Token a partir de um Refresh Token válido.

### Crianças (`/api/v1/children`)
Protegidos. Exigem que o usuário possua suas próprias entidades e não acesse registros de outros.
- `GET /`: Lista todas as crianças pertencentes ao usuário logado.
- `POST /`: Adiciona uma nova criança.
- `GET /{id}`: Retorna dados de uma criança específica do usuário.
- `PUT /{id}`: Atualiza os dados de uma criança do usuário.
- `DELETE /{id}`: Desativa a criança (Exclusão Lógica/Soft Delete).

### Vacinas e Doses (`/api/v1/vaccines`)
Geralmente públicos ou disponíveis para consulta de usuários logados (dependendo da configuração).
- `GET /`: Lista todas as vacinas ativas.
- `GET /{id}`: Traz os detalhes de uma vacina incluindo seu esquema de doses.

### Acompanhamento de Doses e Agendamento (`/api/v1/vaccination-records` e `/api/v1/vaccination-schedules`)
Protegidos.
- `POST /vaccination-records`: Registra que uma dose foi aplicada em uma criança em uma data específica.
- `GET /vaccination-schedules/child/{childId}`: Busca o esquema vacinal (pendente, atrasado e concluído) gerado a partir do calendário da criança.

### Campanhas de Vacinação (`/api/v1/campaigns`)
- **Admin**: Endpoints administrativos (`POST`, `PUT`, `DELETE`) em `/api/v1/admin/campaigns` protegidos com papel `ROLE_ADMIN`.
- **Público/Usuários**: A consulta por campanhas ativas (`GET /api/v1/campaigns`) está disponível de forma leitura.

## Tratamento de Erros
A API segue um formato padrão para retornar falhas utilizando `ProblemDetail` do Spring Boot (RFC 7807).

- **400 Bad Request**: Erros de validação nos dados enviados (ex: datas incorretas, tamanho de senha, obrigatoriedade de campos).
- **401 Unauthorized**: JWT expirado, ausente ou inválido.
- **403 Forbidden**: Sem permissão de acesso (ex: um `USER` tentando chamar endpoint de `ADMIN` ou acessar a criança de outro usuário).
- **404 Not Found**: Recurso não localizado (ex: `id` não existe no banco ou não pertence ao usuário logado).
- **409 Conflict**: Registro duplicado ou tentativa de conflito lógico no sistema (ex: e-mail já cadastrado, ou registro de vacina duplicado).
- **422 Unprocessable Entity**: Violação de regras de negócio (ex: Data final da campanha anterior à data de início).
- **500 Internal Server Error**: Exceção interna não tratada.
