package com.lucas.vacinakids.vaccine.dto;

import java.util.List;
import java.util.UUID;

public record VaccineResponse(
    UUID id,
    String name,
    String description,
    List<VaccineDoseResponse> doses
) {}
