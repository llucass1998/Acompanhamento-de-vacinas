# Arquitetura atual

## Visão de execução

```mermaid
flowchart LR
    U[Usuário / navegador não confiável] --> V[Vercel / SPA Angular-Ionic]
    V -->|HTTPS pretendido; URL prod ainda placeholder| A[API Spring Boot]
    A --> S[Spring Security + filtro JWT]
    S --> C[Controllers + Bean Validation]
    C --> D[Services transacionais]
    D --> R[Spring Data repositories]
    R --> P[(PostgreSQL 16)]
    V --> L[(localStorage: access, refresh, user, childId)]
    A --> O[Actuator / Swagger]
```

O frontend de produção publicado não está conectado a uma URL real de backend no código auditado. O Compose inicia somente PostgreSQL; backend e frontend não formam uma stack Docker completa.

## Fluxo de autenticação atual

```mermaid
sequenceDiagram
    participant B as Navegador
    participant A as AuthController
    participant S as AuthService
    participant DB as PostgreSQL

    B->>A: POST /auth/login (email, senha)
    A->>S: login DTO validado
    S->>DB: usuário por email
    S->>S: BCrypt via AuthenticationManager
    S->>S: JWT HMAC (sub=id, email, iat, exp)
    S->>S: UUID aleatório como refresh; SHA-256
    S->>DB: grava somente token_hash
    S-->>B: accessToken + refreshToken no JSON
    B->>B: grava ambos em localStorage
```

No refresh, o backend busca o hash, verifica expiração/revogação, revoga o token antigo e cria outro. Não há família de tokens, detecção de reutilização nem trava contra duas renovações simultâneas. O frontend não chama `/refresh`.

## Autenticação de cada request

```mermaid
sequenceDiagram
    participant B as Cliente
    participant F as AuthTokenFilter
    participant J as JwtUtils
    participant U as UserDetailsService
    participant API as Endpoint

    B->>F: Authorization: Bearer JWT
    F->>J: verifica assinatura e expiração
    J-->>F: claim email
    F->>U: carrega usuário/role atuais do banco
    F->>F: cria Authentication
    F->>API: prossegue
```

Ponto positivo: role é recarregada do banco e não confiada ao frontend. Lacuna: o filtro cria autenticação mesmo quando `UserDetails.isEnabled()` é falso; um access token emitido antes da desativação pode continuar aceito até expirar.

## Fluxo de autorização de dados privados

```mermaid
flowchart TD
    JWT[JWT válido] --> P[Controller recebe UserDetailsImpl]
    P -->|passa userId como parâmetro| SV[Service]
    SV --> VR{Valida criança por id + userId?}
    VR -->|sim| Q[Repository consulta por childId]
    VR -->|não| N[404]
    Q --> DB[(Banco sem RLS)]
```

Controllers de criança extraem o ID do principal e os repositories de criança filtram por usuário. Calendário e registros validam a criança primeiro e depois consultam por `childId`. Services, porém, aceitam qualquer `userId` fornecido pelo chamador e o banco não oferece RLS; uma chamada interna incorreta não possui segunda barreira independente.

## Modelo de dados

```mermaid
erDiagram
    USERS ||--o{ CHILDREN : owns
    USERS ||--o{ REFRESH_TOKENS : has
    CHILDREN ||--o{ VACCINATION_SCHEDULES : has
    CHILDREN ||--o{ VACCINATION_RECORDS : has
    VACCINES ||--o{ VACCINE_DOSES : contains
    VACCINE_DOSES ||--o{ VACCINATION_SCHEDULES : schedules
    VACCINE_DOSES ||--o{ VACCINATION_RECORDS : records

    USERS {
      uuid id PK
      varchar email UK
      varchar password_hash
      varchar role
      boolean active
    }
    CHILDREN {
      uuid id PK
      uuid user_id FK
      date birth_date
      boolean active
    }
    REFRESH_TOKENS {
      uuid id PK
      uuid user_id FK
      varchar token_hash UK
      timestamptz expires_at
      timestamptz revoked_at
    }
    VACCINATION_SCHEDULES {
      uuid id PK
      uuid child_id FK
      uuid dose_id FK
      date expected_date
      varchar status
    }
    VACCINATION_RECORDS {
      uuid id PK
      uuid child_id FK
      uuid dose_id FK
      date applied_date
    }
```

`campaigns` é independente e possui `CHECK (end_date >= start_date)`.

## Contrato frontend versus backend

| Fluxo | Frontend atual | Backend real | Resultado esperado |
|---|---|---|---|
| Criança | envia `gender`, não envia `responsibleName` | exige `responsibleName` | 400 |
| Agenda | `/vaccination-schedules/child/{id}` | `/children/{id}/vaccination-schedule` | 401/404 conforme segurança/rota; não integra |
| Resumo | `/vaccination-schedules/child/{id}/summary` | `/children/{id}/vaccination-summary` | não integra |
| Histórico | `/vaccination-records/child/{id}` | `/children/{id}/records` | não integra |
| Registrar dose | body contém `childId`, `vaccineId`, `notes` | childId no path; body `doseId`, `appliedDate`, `location`, `batchNumber`, `observations` | não integra |
| Produção | `sua-api-producao.com` | nenhuma URL registrada | frontend sem backend válido |
