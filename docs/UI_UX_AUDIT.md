# Vacina Kids UI/UX Audit

## Informações do Projeto
- **Repositório raiz**: `/c/Acompanhamento-de-vacinas-main`
- **Branch atual**: `master`
- **Framework frontend**: Angular 18 com Ionic
- **Estado atual**: O frontend compila e passa nos testes, o backend compila e está integrado.
- **UI/UX Status**: O projeto possui componentes próprios não padronizados através de todos os cards. Cores estão com variáveis locais e globais duplicadas ou desorganizadas.

## Estrutura do Frontend
A aplicação usa Ionic Components (`ion-card`, `ion-button`, `ion-input`) mas muitos templates estão usando tags HTML limpas ou ignorando o shadow-dom do Ionic pelo que o compilador aponta em dezenas de *Warnings*. O log de build mostra que os componentes do Ionic (`IonCard`, `IonBadge`, etc) não estão sendo usados nos templates ou não foram importados corretamente dentro dos *standalone components*.

## Análise Visual (Global)

### Cores e Tematização (Médio)
- `variables.scss` atual possui variáveis customizadas (`--app-green`, `--app-yellow`) convivendo com variáveis do Ionic, porém cores estão hardcoded ou com variações opacas em `global.scss`.
- A paleta não transmite totalmente um ambiente médico/saúde (Uso excessivo de laranja vibrante e amarelão).

### Tipografia e Layout (Alto)
- A tipografia em `global.scss` está usando *Inter* com fallbacks. Mas os tamanhos não estão centralizados em tokens.
- O Layout mistura Ionic Grid com classes `.grid` puras. Isso gera responsividade manual inconsistente.

### Componentização (Crítico)
- Avisos severos do compilador Angular (`NG8113`) apontam que os componentes do Ionic estão declarados nos imports do `@Component` mas não são usados no template HTML. Isso significa que as páginas (`AcompanhamentoPage`, `CampanhasPage`, `CriancaPage`, `HistoricoPage`) provavelmente usam HTML customizado sem aproveitar a robustez dos componentes nativos do Ionic ou estão importando errado.
- Não há diretório `shared/components/`. A repetição de HTML é quase certa.

### Acessibilidade & UX (Alto)
- Não existe estado claro de *Loading* ou *Empty* na maioria das telas. O `global.scss` prevê classes como `.empty-state`, mas precisam ser padronizadas.
- Tamanho das áreas de toque: os botões e links customizados podem sofrer no mobile por ausência de padding padronizado.

## Checklist de Problemas

- [x] **Crítico**: Resolver warnings de imports não utilizados no Angular Compiler.
- [x] **Crítico**: Criar estrutura de componentes reutilizáveis (`shared/components`).
- [x] **Alto**: Substituir variáveis de cor hardcoded e soltas no `global.scss` por um Design System no `variables.scss`.
- [x] **Alto**: Garantir Feedback Visual (Loading, Empty, Error) para chamadas de API do Backend em todas as telas (`Acompanhamento`, `Campanhas`, `Criança`, `Histórico`).
- [x] **Médio**: Refatorar o HTML para usar corretamente os componentes Ionic (`ion-card`, `ion-item`, `ion-button`).
