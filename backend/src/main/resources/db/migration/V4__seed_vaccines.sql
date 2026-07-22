-- Seed demonstrativo de vacinas (Baseado no PNI - Brasil)
DO $$
DECLARE
    v_bcg UUID := uuid_generate_v4();
    v_hepb UUID := uuid_generate_v4();
    v_penta UUID := uuid_generate_v4();
    v_polio UUID := uuid_generate_v4();
    v_rota UUID := uuid_generate_v4();
    v_pneumo UUID := uuid_generate_v4();
    v_menin UUID := uuid_generate_v4();
    v_febre UUID := uuid_generate_v4();
    v_triplice UUID := uuid_generate_v4();
    v_tetra UUID := uuid_generate_v4();
    v_hepa UUID := uuid_generate_v4();
    v_hpv UUID := uuid_generate_v4();
BEGIN
    INSERT INTO vaccines (id, name, description, active, created_at, updated_at) VALUES
    (v_bcg, 'BCG', 'Protege contra as formas graves da tuberculose', true, now(), now()),
    (v_hepb, 'Hepatite B', 'Protege contra a Hepatite B', true, now(), now()),
    (v_penta, 'Pentavalente', 'Protege contra difteria, tétano, coqueluche, hepatite B e infecções causadas por Haemophilus influenzae B', true, now(), now()),
    (v_polio, 'Poliomielite (VIP/VOP)', 'Protege contra a Poliomielite (paralisia infantil)', true, now(), now()),
    (v_rota, 'Rotavírus Humano', 'Protege contra diarreia por rotavírus', true, now(), now()),
    (v_pneumo, 'Pneumocócica 10 Valente', 'Protege contra doenças invasivas e otite média aguda causadas por Streptococcus pneumoniae', true, now(), now()),
    (v_menin, 'Meningocócica C', 'Protege contra a Doença Meningocócica C', true, now(), now()),
    (v_febre, 'Febre Amarela', 'Protege contra a Febre Amarela', true, now(), now()),
    (v_triplice, 'Tríplice Viral', 'Protege contra sarampo, caxumba e rubéola', true, now(), now()),
    (v_tetra, 'Tetraviral', 'Protege contra sarampo, caxumba, rubéola e varicela', true, now(), now()),
    (v_hepa, 'Hepatite A', 'Protege contra a Hepatite A', true, now(), now()),
    (v_hpv, 'HPV', 'Protege contra o Papilomavírus Humano', true, now(), now());

    INSERT INTO vaccine_doses (vaccine_id, dose_name, recommended_age_months, description, source, source_version, active, created_at, updated_at) VALUES
    (v_bcg, 'Dose Única', 0, 'Ao nascer', 'PNI', '2026', true, now(), now()),
    (v_hepb, 'Dose Única', 0, 'Ao nascer', 'PNI', '2026', true, now(), now()),
    (v_penta, '1ª Dose', 2, 'Aos 2 meses', 'PNI', '2026', true, now(), now()),
    (v_penta, '2ª Dose', 4, 'Aos 4 meses', 'PNI', '2026', true, now(), now()),
    (v_penta, '3ª Dose', 6, 'Aos 6 meses', 'PNI', '2026', true, now(), now()),
    (v_polio, '1ª Dose (VIP)', 2, 'Aos 2 meses', 'PNI', '2026', true, now(), now()),
    (v_polio, '2ª Dose (VIP)', 4, 'Aos 4 meses', 'PNI', '2026', true, now(), now()),
    (v_polio, '3ª Dose (VIP)', 6, 'Aos 6 meses', 'PNI', '2026', true, now(), now()),
    (v_polio, '1º Reforço (VOP)', 15, 'Aos 15 meses', 'PNI', '2026', true, now(), now()),
    (v_polio, '2º Reforço (VOP)', 48, 'Aos 4 anos', 'PNI', '2026', true, now(), now()),
    (v_rota, '1ª Dose', 2, 'Aos 2 meses', 'PNI', '2026', true, now(), now()),
    (v_rota, '2ª Dose', 4, 'Aos 4 meses', 'PNI', '2026', true, now(), now()),
    (v_pneumo, '1ª Dose', 2, 'Aos 2 meses', 'PNI', '2026', true, now(), now()),
    (v_pneumo, '2ª Dose', 4, 'Aos 4 meses', 'PNI', '2026', true, now(), now()),
    (v_pneumo, 'Reforço', 12, 'Aos 12 meses', 'PNI', '2026', true, now(), now()),
    (v_menin, '1ª Dose', 3, 'Aos 3 meses', 'PNI', '2026', true, now(), now()),
    (v_menin, '2ª Dose', 5, 'Aos 5 meses', 'PNI', '2026', true, now(), now()),
    (v_menin, 'Reforço', 12, 'Aos 12 meses', 'PNI', '2026', true, now(), now()),
    (v_febre, 'Dose Inicial', 9, 'Aos 9 meses', 'PNI', '2026', true, now(), now()),
    (v_febre, 'Reforço', 48, 'Aos 4 anos', 'PNI', '2026', true, now(), now()),
    (v_triplice, '1ª Dose', 12, 'Aos 12 meses', 'PNI', '2026', true, now(), now()),
    (v_tetra, 'Dose Única', 15, 'Aos 15 meses', 'PNI', '2026', true, now(), now()),
    (v_hepa, 'Dose Única', 15, 'Aos 15 meses', 'PNI', '2026', true, now(), now()),
    (v_hpv, '1ª Dose', 108, 'Meninas de 9 a 14 anos e meninos de 11 a 14', 'PNI', '2026', true, now(), now()),
    (v_hpv, '2ª Dose', 114, '6 meses após a 1ª dose', 'PNI', '2026', true, now(), now());
END $$;
