ALTER TABLE children FORCE ROW LEVEL SECURITY;
ALTER TABLE vaccination_schedules FORCE ROW LEVEL SECURITY;
ALTER TABLE vaccination_records FORCE ROW LEVEL SECURITY;
-- Role attribute changes are performed by the DBA/bootstrap owner, never by the
-- application migration connection. The runtime role must be configured externally.
