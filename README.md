# Vacina Kids

Aplicação Ionic com Angular para acompanhamento da jornada de vacinação infantil. O projeto simula uma carteira digital simples para famílias com mais de uma criança.

## Funcionalidades

- Cadastro e seleção de crianças.
- Acompanhamento individual da situação vacinal.
- Indicadores de vacinas tomadas, pendentes e atrasadas.
- Registro de vacina como tomada.
- Histórico vacinal por criança.
- Listagem de campanhas de vacinação ativas e futuras.
- Interface responsiva para desktop, tablet e mobile.

## Paleta Obrigatória

- Verde: `#ABC270`
- Amarelo: `#FEC868`
- Laranja: `#FDA769`
- Marrom: `#473C33`

## Cenários Atendidos

1. O responsável identifica vacinas tomadas e vacinas que precisam de atenção.
2. Vacinas com data prevista ultrapassada aparecem como atrasadas.
3. Campanhas ativas ficam destacadas na tela de campanhas.
4. Cada criança possui resumo e histórico próprios, evitando confusão entre filhos.

## Tecnologias

- Ionic Framework
- Angular
- TypeScript
- Capacitor

## Como Rodar Localmente

```bash
npm install
npm start
```

A aplicação ficará disponível em `http://127.0.0.1:4200/` ou `http://localhost:4200/`.

## Build

```bash
npm run build
```

A saída de produção é gerada em `www/`.

## Publicação na Vercel

O projeto já possui `vercel.json` configurado para:

- comando de build: `npm run build`
- diretório de saída: `www`
- fallback de rotas para SPA

Na Vercel, importe o repositório GitHub e confirme essas configurações caso a plataforma não detecte automaticamente.

## Observação

Os dados são mockados em memória para fins de demonstração do desafio. Firestore/Firebase podem ser adicionados como evolução futura.
