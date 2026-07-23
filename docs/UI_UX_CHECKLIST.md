# UI/UX Checklist de Modernização

## Fases da Modernização

### 1. Preparação & Design System
- [ ] Atualizar `src/theme/variables.scss` com a nova paleta.
- [ ] Atualizar `src/global.scss` para consumir tokens do Design System.
- [ ] Mapear as cores do Ionic para a nova paleta primária.

### 2. Estrutura Global e Componentes
- [ ] Criar pasta `src/app/shared/components`.
- [ ] Criar componente `EmptyState`.
- [ ] Criar componente `StatusChip`.
- [ ] Criar componente `PageHeader`.

### 3. Refatoração de Páginas
- [ ] **Login & Registro**: Aplicar novos cards e tipografia. Melhorar espaçamento.
- [ ] **Tabs / Navegação**: Ajustar tab bar para design clean e profissional.
- [ ] **Crianças (Gestão)**: Atualizar listas e cards para novo visual.
- [ ] **Acompanhamento (Dashboard)**: Resolver avisos do compilador e utilizar `ion-card` corretamente.
- [ ] **Histórico**: Melhorar visualização do timeline/lista de histórico.
- [ ] **Campanhas**: Polir os cards de aviso e campanhas de saúde.

### 4. Feedback & Segurança Visual
- [ ] Validar Loadings em todas as requisições API.
- [ ] Validar Toast/Alerts de erros mantendo as mensagens fiéis à API.
- [ ] Checar responsividade em todas as rotas principais.
