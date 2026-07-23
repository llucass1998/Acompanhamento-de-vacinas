package com.lucas.vacinakids.officialsource.repository;

import com.lucas.vacinakids.officialsource.entity.OfficialSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OfficialSourceRepository extends JpaRepository<OfficialSource, UUID> {
}
