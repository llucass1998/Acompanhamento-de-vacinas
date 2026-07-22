# Discussão — Fase 5

Os services já obtêm o userId do principal no controller e usam repositories com filtro de proprietário para crianças. O reforço desta fase concentra-se em invariantes de domínio: data de dose não pode anteceder nascimento, textos são normalizados e operações permanecem transacionais. Concorrência/UNIQUE será fechada na Fase 6.
