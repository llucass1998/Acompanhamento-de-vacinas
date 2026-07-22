# Vacina Kids

Aplicacao Ionic + Angular para acompanhamento da vacinacao infantil. O projeto permite que uma familia acompanhe criancas diferentes, visualize vacinas previstas, identifique pendencias e consulte o historico vacinal.

Deploy: https://acompanhamento-de-vacinas.vercel.app/tabs/acompanhamento

## Funcionalidades principais

- Cadastro e selecao de criancas.
- Visualizacao individual da situacao vacinal de cada crianca.
- Listagem de vacinas com nome, dose, descricao, data prevista e status.
- Identificacao de vacinas tomadas, pendentes e atrasadas.
- Registro de vacina como tomada.
- Consulta do historico vacinal por crianca.
- Exibicao de campanhas de vacinacao.
- Persistencia local dos dados no navegador.

## Cenarios atendidos

### Cenario 1

Uma crianca possui vacinas previstas para sua faixa etaria. O responsavel consegue identificar facilmente quais vacinas ja foram realizadas e quais ainda precisam de atencao.

### Cenario 2

Uma vacina com data prevista ultrapassada e ainda nao aplicada aparece com status de atrasada e destaque visual.

### Cenario 3

Campanhas de vacinacao ativas sao exibidas na tela de campanhas, com publico e periodo informados.

### Cenario 4

Uma familia com mais de um filho consegue selecionar cada crianca e visualizar historico, vacinas e resumo de forma individual.

## Tecnologias

- Ionic Framework
- Angular
- TypeScript
- Capacitor
- Jasmine/Karma
- Vercel

## Como rodar localmente

```bash
npm install
npm start
```

A aplicacao ficara disponivel em `http://localhost:4200/`.

## Validacao

```bash
npm test -- --watch=false --browsers=ChromeHeadless
npm run lint
npm run build
```

## Deploy

```bash
npm run build
```

A saida de producao e gerada na pasta `www/`.

O arquivo `vercel.json` ja esta configurado para publicar a aplicacao como SPA na Vercel.

## Observacao

Este projeto tem finalidade educacional. As informacoes de vacinas servem como simulacao para acompanhamento e devem ser sempre confirmadas com a caderneta oficial e uma unidade de saude.
