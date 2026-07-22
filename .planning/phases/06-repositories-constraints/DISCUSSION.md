# Discussão — Fase 6

Os repositories privados já possuem métodos por `userId` para crianças e validação de propriedade nos services. A integridade SQL tinha chaves e algumas UNIQUE, mas não restringia role, status, idade recomendada ou texto em branco. A migration V8 adiciona constraints defensivas sem remover dados.
