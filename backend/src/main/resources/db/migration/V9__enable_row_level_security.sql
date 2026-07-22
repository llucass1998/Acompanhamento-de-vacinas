-- Runtime roles are intentionally distinct from the migration/owner role.
DO $$ BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'vacina_app') THEN
        CREATE ROLE vacina_app NOLOGIN NOSUPERUSER NOBYPASSRLS;
    END IF;
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'vacina_migrator') THEN
        CREATE ROLE vacina_migrator NOLOGIN NOSUPERUSER NOBYPASSRLS;
    END IF;
END $$;

ALTER TABLE children ENABLE ROW LEVEL SECURITY;
ALTER TABLE vaccination_schedules ENABLE ROW LEVEL SECURITY;
ALTER TABLE vaccination_records ENABLE ROW LEVEL SECURITY;
ALTER TABLE refresh_tokens ENABLE ROW LEVEL SECURITY;

CREATE POLICY children_owner_policy ON children
    USING (user_id::text = current_setting('app.current_user_id', true));
CREATE POLICY schedules_owner_policy ON vaccination_schedules
    USING (EXISTS (SELECT 1 FROM children c WHERE c.id = child_id
                   AND c.user_id::text = current_setting('app.current_user_id', true)));
CREATE POLICY records_owner_policy ON vaccination_records
    USING (EXISTS (SELECT 1 FROM children c WHERE c.id = child_id
                   AND c.user_id::text = current_setting('app.current_user_id', true)));
CREATE POLICY refresh_tokens_owner_policy ON refresh_tokens
    USING (user_id::text = current_setting('app.current_user_id', true));

GRANT SELECT, INSERT, UPDATE, DELETE ON children, vaccination_schedules,
    vaccination_records, refresh_tokens, users, vaccines, vaccine_doses, campaigns TO vacina_app;
