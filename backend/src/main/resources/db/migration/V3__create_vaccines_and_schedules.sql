CREATE TABLE vaccines (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE vaccine_doses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    vaccine_id UUID NOT NULL REFERENCES vaccines(id),
    dose_name VARCHAR(120) NOT NULL,
    recommended_age_months INTEGER NOT NULL,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    source VARCHAR(100),
    source_version VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_vaccine_doses_vaccine_id ON vaccine_doses(vaccine_id);

CREATE TABLE vaccination_schedules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    child_id UUID NOT NULL REFERENCES children(id),
    vaccine_dose_id UUID NOT NULL REFERENCES vaccine_doses(id),
    expected_date DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (child_id, vaccine_dose_id)
);

CREATE INDEX idx_vaccination_schedules_child_id ON vaccination_schedules(child_id);
