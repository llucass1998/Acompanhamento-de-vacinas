# Vacina Kids — contexto do projeto

## Propósito

O Vacina Kids apoia responsáveis no acompanhamento do calendário vacinal infantil: cadastro de crianças, visualização de doses previstas, registro de aplicações, histórico e consulta de campanhas. O backend também oferece administração de campanhas.

O sistema trata dados pessoais de responsáveis e crianças, datas de nascimento, situação vacinal, datas e locais de aplicação, observações, credenciais, papéis de acesso e tokens de sessão. Esses dados exigem confidencialidade, integridade, disponibilidade e isolamento por conta.

## Usuários

- Responsável autenticado (`USER`): administra somente as próprias crianças e respectivos dados vacinais.
- Administrador (`ADMIN`): administra campanhas por endpoints próprios.
- Operadores técnicos: executam build, migrations, operação e recuperação; ainda não há papéis técnicos separados no PostgreSQL.

## Estado técnico auditado

- Frontend: Angular 20.3, Ionic 8, TypeScript 5.9 e Capacitor 8, como aplicação standalone.
- Backend: Java com target/release 21, Spring Boot 3.4.2, Spring Security, JPA/Hibernate, Bean Validation, Flyway e JWT com JJWT.
- Banco: PostgreSQL 16, sete migrations Flyway.
- Infraestrutura: Vercel para o frontend; Dockerfile multi-stage para o backend; Compose contém somente PostgreSQL.
- Testes: JUnit/Mockito/MockMvc e Jasmine/Karma. O código contém suporte a Testcontainers, mas a suíte normal usa um PostgreSQL em URL fixa no profile de teste.
- CI/CD: não há `.github/workflows`, Dependabot ou scanners versionados.

Auditoria iniciada em `master` no commit `821e5ea`. Durante a execução, mudanças concorrentes avançaram a mesma branch até `12fe3da` e integraram um novo frontend HTTP. O mapa final considera `12fe3da`; duas alterações locais concorrentes de configuração do backend permaneceram fora do commit documental.

## Fronteiras de confiança

1. Navegador/Capacitor → frontend: ambiente não confiável e controlável pelo usuário.
2. Frontend/Vercel → API Spring: toda entrada, token, ID, role, header e JSON é não confiável.
3. Spring MVC/Security → services: controllers e filtros podem conter falhas; o domínio deve reaplicar identidade, propriedade e invariantes.
4. Services → repositories: uma consulta pode omitir o proprietário; o repositório e o banco precisam limitar o impacto.
5. Aplicação → PostgreSQL: o processo da aplicação não deve ser dono, superuser nem possuir `BYPASSRLS`.
6. Código/dependências → build/CI → imagens: alterações e componentes de terceiros são não confiáveis até serem verificados.
7. Operação → logs/backups: observabilidade não pode vazar segredos e a recuperação precisa ser testada.

## Escopo deste ciclo

Inclui frontend, backend, autenticação, autorização, contratos, domínio, repositories, migrations, PostgreSQL, Docker, edge, dependências, CI/CD, logs, auditoria, backup e testes de defesa entre camadas.

Não inclui aconselhamento clínico, substituição da caderneta oficial, validação oficial do calendário do PNI, contratação/configuração de serviços externos, deploy, push, merge ou gestão de branch protection fora do repositório.

## Aviso de saúde

O Vacina Kids é um sistema de apoio e não substitui a caderneta oficial, profissionais de saúde, unidades de saúde nem orientações oficiais. Datas, campanhas e condutas devem ser confirmadas em fontes oficiais.
