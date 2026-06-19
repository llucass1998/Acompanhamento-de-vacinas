import { Routes } from '@angular/router';
import { TabsPage } from './tabs.page';

export const routes: Routes = [
  {
    path: 'tabs',
    component: TabsPage,
    children: [
      {
        path: 'crianca',
        loadComponent: () =>
          import('./pages/crianca/crianca.page').then(
            (m) => m.CriancaPage
          ),
      },
      {
        path: 'acompanhamento',
        loadComponent: () =>
          import('./pages/acompanhamento/acompanhamento.page').then(
            (m) => m.AcompanhamentoPage
          ),
      },
      {
        path: 'historico',
        loadComponent: () =>
          import('./pages/historico/historico.page').then(
            (m) => m.HistoricoPage
          ),
      },
      {
        path: 'campanhas',
        loadComponent: () =>
          import('./pages/campanhas/campanhas.page').then(
            (m) => m.CampanhasPage
          ),
      },
      {
        path: '',
        redirectTo: '/tabs/crianca',
        pathMatch: 'full',
      },
    ],
  },
  {
    path: '',
    redirectTo: '/tabs/crianca',
    pathMatch: 'full',
  },
];