# CHECKPOINT — FASE 6

## Resumo das Implementações

A **Fase 6 — Campanhas e Área Administrativa** foi concluída com sucesso. Os principais componentes criados ou alterados foram:

1. **Entidade `Campaign`:**
   - Criação da tabela `campaigns` via migration `V7__create_campaigns.sql`.
   - Adicionada a constraint no banco de dados para garantir que a data final não seja inferior à data inicial.
   - Adicionados índices em datas e em `active` para tornar as buscas mais performáticas.

2. **Repositório (`CampaignRepository`):**
   - Criação das consultas baseadas na data atual com suporte à paginação:
     - Campanhas **ativas** (`findActiveCampaignsAtDate`).
     - Campanhas **futuras ou ativas** (`findUpcomingOrActiveCampaigns`).

3. **Serviços (`CampaignService`):**
   - Lógica de tratamento da criação e modificação de campanhas.
   - Validações de domínio usando exceptions customizadas como `BusinessException` (422) e `NotFoundException` (404), integradas com o `GlobalExceptionHandler`.
   - Transformação do status dinâmico na leitura (ex: "UPCOMING", "ONGOING", "FINISHED").

4. **Controllers:**
   - `CampaignController` (`/api/v1/campaigns`): Acesso autenticado livre, restrito para leitura apenas para listar campanhas ativas e futuras.
   - `AdminCampaignController` (`/api/v1/admin/campaigns`): Restrito pelo papel `ADMIN` através da anotação `@PreAuthorize`. Permite total gerência: cadastrar, alterar e (des)ativar campanhas.

5. **Testes e Qualidade:**
   - `CampaignServiceTest` e `CampaignControllerIntegrationTest` implementados.
   - Foram validadas as respostas de segurança (403 para criação usando conta ROLE_USER e 201 com ROLE_ADMIN).
   - Testada regra de negócio de bloqueio de datas invertidas.

---

O build está verde e o projeto está pronto para iniciar a documentação e contrato da API (Fase 7).
