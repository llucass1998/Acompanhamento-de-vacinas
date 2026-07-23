# Checkpoint Fase 9 - Estatísticas Públicas PNI

## O que foi implementado
1. **Entidades Estatísticas**:
   - `PniStatisticsSnapshot` criado mapeando as colunas exatas da migração `V11` (e garantindo rastreabilidade do source no banco via `OfficialSource`).
2. **Consultas (Repository & DTO)**:
   - `PniStatisticsSnapshotRepository` oferecendo paginação baseada em `reference_year`.
   - Conversão segura (sem vazar Entidade) gerida pela class record `PniStatisticsSnapshotResponse`.
3. **Controladores e Segurança**:
   - Criado `PublicStatisticsController` em `/api/v1/public/statistics`.
   - Modificado `SecurityConfig` para permitir acessos públicos (não autenticados) às rotas `/api/v1/public/**`.
4. **Testes de Integração**:
   - Escrito e validado `PublicStatisticsControllerIntegrationTest` mockando snapshots demográficos, disparando uma chamada `GET` anônima e comprovando o `200 OK` e o encapsulamento dos dados no JSON.

## Próximos Passos
Avançar para a **Fase 10** final. Processo de validação de ambiente, Bootstrap Seguro (`BOOTSTRAP_ADMIN_ENABLED=true`), revisão de todos os princípios (Não usar Create ddl-auto em PROD, regras imutáveis), e preparar tudo para Push do Git no GitHub.