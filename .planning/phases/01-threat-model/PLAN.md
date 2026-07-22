# Plano — Fase 1: Modelo de ameaças

| Tarefa | Arquivos | Risco | Testes/evidência | Critério |
|---|---|---|---|---|
| T1. Consolidar fronteiras de confiança | `docs/THREAT_MODEL.md` | Escopo ambíguo | Revisão contra arquitetura auditada | Todas as entradas não confiáveis explícitas |
| T2. Mapear fluxos principais | `docs/THREAT_MODEL.md` | Fluxo sem controle | Diagramas Mermaid | Usuário→frontend→API→DB, arquivos e CI cobertos |
| T3. Registrar casos de abuso | `docs/THREAT_MODEL.md` | Ameaça esquecida | Checklist dos casos obrigatórios | Todos os abusos pedidos classificados |
| T4. Definir controles e detecção | `docs/THREAT_MODEL.md` | Risco crítico com camada única | Matriz preventiva/detectiva | Críticos têm ≥3 camadas planejadas |
| T5. Atualizar estado/checklist | `STATE.md`, `ROADMAP.md`, checklist | Gate falso | `git diff --check`, revisão documental | Fase aprovada somente com evidências |

Não haverá alteração de código nesta fase. Correções serão executadas nas fases correspondentes.
