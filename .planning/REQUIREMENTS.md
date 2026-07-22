# Requisitos funcionais existentes

Legenda: `IMPLEMENTADO`, `PARCIAL`, `NÃO INTEGRADO` ou `NÃO VERIFICADO` descreve o código auditado, não uma promessa futura.

| ID | Requisito | Estado e evidência |
|---|---|---|
| RF-01 | Cadastro de usuário comum | `IMPLEMENTADO` no backend em `POST /api/v1/auth/register`; role é fixada como `USER`. Tela frontend existe. |
| RF-02 | Login | `IMPLEMENTADO` no backend com BCrypt, access JWT e refresh opaco; tela frontend existe. |
| RF-03 | Identificação do responsável autenticado | `PARCIAL`: backend possui `User`; não há recurso separado de responsáveis/dependentes adultos. |
| RF-04 | CRUD de crianças por conta | `IMPLEMENTADO` no backend com filtro por `userId`; frontend recém-integrado envia contrato incompatível (`gender` em vez de `responsibleName`). |
| RF-05 | Calendário vacinal por criança | `IMPLEMENTADO` no backend em `/api/v1/children/{childId}/vaccination-schedule`; frontend chama paths diferentes. |
| RF-06 | Resumo vacinal | `IMPLEMENTADO` no backend em `/api/v1/children/{childId}/vaccination-summary`; frontend espera nomes de campos diferentes. |
| RF-07 | Registro de dose | `IMPLEMENTADO` no backend em `/api/v1/children/{childId}/records`; frontend chama `/vaccination-records` e envia payload incompatível. |
| RF-08 | Histórico por criança | `IMPLEMENTADO` no backend no mesmo recurso de records; frontend usa path incompatível. |
| RF-09 | Consulta de vacinas e doses | `IMPLEMENTADO` para usuário autenticado em `/api/v1/vaccines`. |
| RF-10 | Consulta de campanhas | `IMPLEMENTADO` para usuário autenticado em `/api/v1/campaigns`; documentação anterior chama o recurso de público, mas a cadeia exige autenticação. |
| RF-11 | Administração de campanhas | `IMPLEMENTADO` em `/api/v1/admin/campaigns`, com regra de rota e `@PreAuthorize` no controller. |
| RF-12 | Logout | `PARCIAL`: backend revoga refresh tokens do usuário, mas o frontend apenas apaga `localStorage` e não chama o endpoint. |
| RF-13 | Renovação de sessão | `PARCIAL`: backend rotaciona o refresh token; frontend armazena o token, mas não usa `/refresh`. |
| RF-14 | Persistência central | `NÃO INTEGRADO`: o backend persiste em PostgreSQL, porém o frontend de produção aponta para `https://sua-api-producao.com/api/v1`. |

## Regras funcionais observadas

- E-mail é normalizado no service antes de cadastro/login.
- Criança é desativada por soft delete.
- Nascimento e data de aplicação futura são rejeitados por Bean Validation.
- Uma dose por criança é única em `vaccination_records` e `vaccination_schedules`.
- Campanha rejeita `endDate < startDate` no service e no banco.
- Calendários são gerados pelo service, mas não foi encontrada chamada automática a `generateScheduleForChild` no fluxo de criação de criança.

## Lacunas funcionais que bloqueiam o baseline

- `npm run lint` falha com 16 erros no HEAD `12fe3da`.
- Contratos e paths do frontend não correspondem à API real.
- O build de produção usa um host de API placeholder.
- A suíte frontend possui apenas dois testes e não exerce os fluxos HTTP recém-adicionados.
