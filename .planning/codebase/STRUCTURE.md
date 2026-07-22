# Estrutura do repositório

```text
C:\Acompanhamento-de-vacinas-main
├── src/                         # frontend Angular/Ionic
│   ├── app/
│   │   ├── guards/              # AuthGuard de navegação
│   │   ├── interceptors/        # Bearer interceptor
│   │   ├── models/              # contratos TypeScript
│   │   ├── pages/auth/          # login e cadastro
│   │   ├── services/            # auth, child, vaccine, record, campaign
│   │   └── tabs/pages/          # crianças, acompanhamento, histórico, campanhas
│   ├── environments/            # URLs de API
│   └── assets/
├── backend/
│   ├── src/main/java/com/lucas/vacinakids/
│   │   ├── auth/                # login, JWT, refresh e UserDetails
│   │   ├── campaign/            # campanhas públicas/autenticadas e admin
│   │   ├── child/               # crianças por usuário
│   │   ├── vaccination/         # calendário e resumo
│   │   ├── vaccinationrecord/   # registro e histórico
│   │   ├── vaccine/             # vacinas e doses
│   │   ├── user/                # User e Role
│   │   └── shared/              # SecurityConfig, OpenAPI e exceptions
│   ├── src/main/resources/
│   │   ├── application*.yml
│   │   └── db/migration/V1..V7
│   ├── src/test/                # 38 testes no baseline validado
│   └── Dockerfile
├── docs/                        # documentação técnica e de segurança
├── .planning/                   # contexto GSD manual deste ciclo
├── docker-compose.yml           # somente PostgreSQL
├── angular.json
├── package.json / package-lock.json
└── vercel.json
```

Não existe diretório frontend separado: o frontend ocupa a raiz do monorepo. Frontend e backend estão, portanto, na mesma branch Git.

Não existem `.github/`, Dockerfile do frontend, configuração de proxy reverso, scripts de backup ou diretório versionado de plataformas Capacitor.
