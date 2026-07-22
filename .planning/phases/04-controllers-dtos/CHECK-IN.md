# CHECK-IN GSD DE SEGURANÇA — FASE 4

Status: APROVADA

### Controles

- Limites de tamanho adicionados a CampaignRequest, VaccinationRecordRequest, LoginRequest e TokenRefreshRequest.
- Jackson configurado para rejeitar campos desconhecidos.
- Pageable limitado a 50 itens.
- DTOs continuam sem `userId`, `ownerId` ou `role`.

### Testes

- Backend `./mvnw.cmd -ntp test`: 38 testes PASSARAM.
- Payload inválido de data continua bloqueado pelo domínio com status 422.
- Campos extras, tamanhos e IDs indevidos: controles configurados; testes dedicados permanecem para evolução.

### Vulnerabilidades

Nenhuma nova. F0-031 permanece aberto.

### Commit

`git commit -m "security: restringe contratos e validacao da api"`

### Próxima fase

Fase 5 — Services e domínio — aguarda novo `CONTINUAR`.
