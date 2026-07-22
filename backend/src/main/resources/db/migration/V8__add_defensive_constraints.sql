ALTER TABLE users
    ADD CONSTRAINT chk_users_role CHECK (role IN ('USER', 'ADMIN'));

ALTER TABLE users
    ADD CONSTRAINT chk_users_email_not_blank CHECK (length(btrim(email)) > 0);

ALTER TABLE children
    ADD CONSTRAINT chk_children_name_not_blank CHECK (length(btrim(name)) > 0),
    ADD CONSTRAINT chk_children_responsible_not_blank CHECK (length(btrim(responsible_name)) > 0);

ALTER TABLE vaccine_doses
    ADD CONSTRAINT chk_vaccine_doses_age_nonnegative CHECK (recommended_age_months >= 0);

ALTER TABLE vaccination_schedules
    ADD CONSTRAINT chk_vaccination_schedule_status CHECK (status IN ('PENDING', 'LATE', 'APPLIED'));

ALTER TABLE vaccination_records
    ADD CONSTRAINT chk_vaccination_records_text_lengths
        CHECK (length(coalesce(location, '')) <= 255
           AND length(coalesce(batch_number, '')) <= 100
           AND length(coalesce(observations, '')) <= 500);
