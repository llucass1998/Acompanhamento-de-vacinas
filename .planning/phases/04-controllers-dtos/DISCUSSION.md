# Discussão — Fase 4

Auditoria encontrou DTOs sem limites em campanhas e registros, paginação sem teto explícito e Jackson aceitando campos desconhecidos. IDs de proprietário não estão nos DTOs atuais; a regra será preservada. Regras de domínio como intervalo de datas permanecem no service para manter o contrato HTTP existente.
