# Discussão — Fase 2: Frontend Angular/Ionic

## Estado de entrada

- Fase 1 aprovada com frontend tratado como não confiável.
- Tokens eram persistidos em `localStorage` e o interceptor anexava bearer a qualquer URL HTTP.
- O backend continua sendo a autoridade de autenticação/autorização.
- Alterações concorrentes fora do escopo permanecem não staged.

## Riscos e decisões

- Access e refresh token passam a existir somente em memória nesta versão web; recarregar a página encerra a sessão local.
- O interceptor só autoriza URLs sob `environment.apiUrl`; chamadas externas nunca recebem bearer.
- AuthGuard permanece somente UX e não é considerado barreira de segurança.
- CSP é aplicada como meta compatível com o bundle atual; headers definitivos devem ser reforçados no edge.
- Refresh automático não foi inventado porque o contrato atual expõe refresh token no JSON e a rotação segura pertence à Fase 8.
