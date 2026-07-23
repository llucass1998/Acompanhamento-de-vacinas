package com.lucas.vacinakids.importdata.repository;

import com.lucas.vacinakids.importdata.entity.ImportItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImportItemRepository extends JpaRepository<ImportItem, UUID> {}
