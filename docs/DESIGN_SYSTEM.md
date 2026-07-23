# Vacina Kids Design System

## Paleta de Cores
O sistema utiliza uma paleta centrada na área de saúde, transmitindo segurança e calma.

```scss
:root {
  --app-primary: #256d5a;
  --app-primary-hover: #1f5b4c;
  --app-primary-active: #194a3e;
  --app-primary-soft: #e8f3ef;

  --app-secondary: #2f6bff;
  --app-secondary-hover: #2458d8;
  --app-secondary-soft: #edf3ff;

  --app-accent: #f4b860;
  --app-accent-soft: #fff5e4;

  --app-background: #f7f9fc;
  --app-surface: #ffffff;
  --app-surface-secondary: #f0f4f8;

  --app-text-primary: #172033;
  --app-text-secondary: #667085;
  --app-text-muted: #98a2b3;
  --app-text-inverse: #ffffff;

  --app-border: #e4e7ec;
  --app-border-strong: #d0d5dd;

  --app-success: #15803d;
  --app-success-soft: #e9f7ee;

  --app-warning: #b7791f;
  --app-warning-soft: #fff7e6;

  --app-danger: #c2413a;
  --app-danger-soft: #fdecec;

  --app-info: #2563eb;
  --app-info-soft: #eaf1ff;
}
```

## Tipografia
- Fonte primária: Inter.
- Hierarquia definida em tokens `--text-*`.

## Componentes Compartilhados (A criar)
- `<app-page-header>`
- `<app-empty-state>`
- `<app-status-chip>`
- `<app-loading-spinner>`

A integração visual usará variáveis css puras conectadas à estrutura base do Ionic.
