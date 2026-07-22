-- 1. Official Sources
CREATE TABLE official_sources (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    source_type VARCHAR(50) NOT NULL CHECK (source_type IN ('NATIONAL_CALENDAR', 'TECHNICAL_CALENDAR', 'NORMATIVE_INSTRUCTION', 'OPEN_DATA_API', 'MANUAL_OFFICIAL_ENTRY')),
    title VARCHAR(255) NOT NULL,
    organization VARCHAR(255) NOT NULL,
    source_url TEXT,
    reference_year INTEGER NOT NULL,
    published_at TIMESTAMP WITH TIME ZONE,
    retrieved_at TIMESTAMP WITH TIME ZONE NOT NULL,
    content_hash VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('ACTIVE', 'SUPERSEDED', 'UNAVAILABLE', 'UNDER_REVIEW')),
    notes TEXT,
    created_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- 2. Calendar Versions
CREATE TABLE calendar_versions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    reference_year INTEGER NOT NULL,
    version VARCHAR(50) NOT NULL,
    description TEXT,
    source_id UUID NOT NULL REFERENCES official_sources(id),
    status VARCHAR(50) NOT NULL CHECK (status IN ('DRAFT', 'UNDER_REVIEW', 'PUBLISHED', 'ARCHIVED')),
    valid_from DATE NOT NULL,
    valid_until DATE,
    published_at TIMESTAMP WITH TIME ZONE,
    published_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version_number BIGINT NOT NULL DEFAULT 0
);

-- 3. Extend Users Table
ALTER TABLE users ADD COLUMN must_change_password BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE users ADD COLUMN last_password_change_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE users ADD COLUMN failed_login_attempts INTEGER NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN locked_until TIMESTAMP WITH TIME ZONE;

-- 4. Extend Vaccines Table
ALTER TABLE vaccines ADD COLUMN code VARCHAR(100) UNIQUE;
ALTER TABLE vaccines ADD COLUMN display_name VARCHAR(255);
ALTER TABLE vaccines ADD COLUMN prevented_diseases TEXT;
ALTER TABLE vaccines ADD COLUMN official BOOLEAN NOT NULL DEFAULT false;
ALTER TABLE vaccines ADD COLUMN source_id UUID REFERENCES official_sources(id);
ALTER TABLE vaccines ADD COLUMN version_number BIGINT NOT NULL DEFAULT 0;

-- Atualiza dados de vacina pré-existentes
UPDATE vaccines SET code = CONCAT('VACCINE_', id), display_name = name, official = false;
ALTER TABLE vaccines ALTER COLUMN code SET NOT NULL;
ALTER TABLE vaccines ALTER COLUMN display_name SET NOT NULL;

-- 5. Extend Vaccine Doses Table
ALTER TABLE vaccine_doses ADD COLUMN code VARCHAR(100);
ALTER TABLE vaccine_doses ADD COLUMN dose_order INTEGER;
ALTER TABLE vaccine_doses ADD COLUMN source_id UUID REFERENCES official_sources(id);
ALTER TABLE vaccine_doses ADD COLUMN version_number BIGINT NOT NULL DEFAULT 0;

UPDATE vaccine_doses SET code = CONCAT('DOSE_', id);
ALTER TABLE vaccine_doses ALTER COLUMN code SET NOT NULL;
ALTER TABLE vaccine_doses ADD CONSTRAINT uk_vaccine_dose_code UNIQUE (vaccine_id, code);

-- 6. Calendar Rules
CREATE TABLE calendar_rules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    calendar_version_id UUID NOT NULL REFERENCES calendar_versions(id),
    vaccine_dose_id UUID NOT NULL REFERENCES vaccine_doses(id),
    life_stage VARCHAR(50) NOT NULL CHECK (life_stage IN ('CHILD', 'ADOLESCENT', 'YOUNG_ADULT', 'ADULT', 'ELDERLY', 'PREGNANT', 'SPECIAL_GROUP')),
    audience VARCHAR(255) NOT NULL,
    recommended_age_days INTEGER,
    recommended_age_months INTEGER,
    recommended_age_years INTEGER,
    minimum_age_days INTEGER,
    maximum_age_days INTEGER,
    interval_after_previous_days INTEGER,
    dose_required BOOLEAN NOT NULL DEFAULT true,
    special_condition TEXT,
    contraindication_notice TEXT,
    professional_notes TEXT,
    public_notes TEXT,
    display_order INTEGER NOT NULL DEFAULT 0,
    source_id UUID NOT NULL REFERENCES official_sources(id),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version_number BIGINT NOT NULL DEFAULT 0
);

-- 7. Extend Vaccination Schedules
ALTER TABLE vaccination_schedules ADD COLUMN calendar_version_id UUID REFERENCES calendar_versions(id);
ALTER TABLE vaccination_schedules ADD COLUMN calendar_rule_id UUID REFERENCES calendar_rules(id);
ALTER TABLE vaccination_schedules ADD COLUMN source_id UUID REFERENCES official_sources(id);
ALTER TABLE vaccination_schedules ADD COLUMN generated_at TIMESTAMP WITH TIME ZONE;

-- 8. Import Jobs
CREATE TABLE import_jobs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    source_id UUID REFERENCES official_sources(id),
    import_type VARCHAR(50) NOT NULL CHECK (import_type IN ('OFFICIAL_CALENDAR', 'OPEN_DATA_STATISTICS', 'MANUAL_FILE', 'MANUAL_ENTRY')),
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'RUNNING', 'WAITING_REVIEW', 'COMPLETED', 'COMPLETED_WITH_WARNINGS', 'FAILED', 'CANCELLED')),
    started_at TIMESTAMP WITH TIME ZONE,
    finished_at TIMESTAMP WITH TIME ZONE,
    records_found INTEGER NOT NULL DEFAULT 0,
    records_created INTEGER NOT NULL DEFAULT 0,
    records_updated INTEGER NOT NULL DEFAULT 0,
    records_rejected INTEGER NOT NULL DEFAULT 0,
    error_message TEXT,
    executed_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- 9. Import Items
CREATE TABLE import_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    import_job_id UUID NOT NULL REFERENCES import_jobs(id),
    entity_type VARCHAR(100) NOT NULL,
    external_identifier VARCHAR(255),
    original_payload JSONB NOT NULL,
    normalized_payload JSONB,
    validation_status VARCHAR(50) NOT NULL CHECK (validation_status IN ('VALID', 'INVALID', 'WARNING', 'DUPLICATE', 'REQUIRES_REVIEW')),
    validation_messages JSONB,
    action VARCHAR(50) CHECK (action IN ('CREATE', 'UPDATE', 'IGNORE', 'REJECT')),
    reviewed_by UUID REFERENCES users(id),
    reviewed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- 10. Admin Audit Logs
CREATE TABLE admin_audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    admin_user_id UUID REFERENCES users(id),
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id UUID,
    before_data JSONB,
    after_data JSONB,
    reason TEXT,
    correlation_id VARCHAR(255),
    ip_hash VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- 11. PNI Statistics Snapshots
CREATE TABLE pni_statistics_snapshots (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    reference_year INTEGER NOT NULL,
    reference_month INTEGER,
    state_code VARCHAR(10),
    municipality_code VARCHAR(20),
    vaccine_code VARCHAR(100),
    dose_code VARCHAR(100),
    age_group VARCHAR(100),
    applied_doses BIGINT NOT NULL,
    source_id UUID NOT NULL REFERENCES official_sources(id),
    collected_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_pni_stats_ref_year ON pni_statistics_snapshots(reference_year);
CREATE INDEX idx_pni_stats_ref_month ON pni_statistics_snapshots(reference_month);
CREATE INDEX idx_pni_stats_state ON pni_statistics_snapshots(state_code);
CREATE INDEX idx_pni_stats_city ON pni_statistics_snapshots(municipality_code);
CREATE INDEX idx_pni_stats_vaccine ON pni_statistics_snapshots(vaccine_code);
