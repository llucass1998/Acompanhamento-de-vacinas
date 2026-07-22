ALTER TABLE vaccination_schedules ADD COLUMN applied_date DATE;
ALTER TABLE vaccination_schedules ADD COLUMN status VARCHAR(255) NOT NULL DEFAULT 'PENDING';
ALTER TABLE vaccination_schedules RENAME COLUMN vaccine_dose_id TO dose_id;
