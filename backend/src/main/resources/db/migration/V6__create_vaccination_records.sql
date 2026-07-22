CREATE TABLE vaccination_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    child_id UUID NOT NULL REFERENCES children(id),
    dose_id UUID NOT NULL REFERENCES vaccine_doses(id),
    applied_date DATE NOT NULL,
    location VARCHAR(255),
    batch_number VARCHAR(100),
    observations VARCHAR(500),
    proof_url VARCHAR(500),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (child_id, dose_id)
);

CREATE INDEX idx_vaccination_records_child_id ON vaccination_records(child_id);
