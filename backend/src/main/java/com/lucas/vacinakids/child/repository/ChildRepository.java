package com.lucas.vacinakids.child.repository;

import com.lucas.vacinakids.child.entity.Child;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChildRepository extends JpaRepository<Child, UUID> {
    Page<Child> findByUserIdAndActiveTrue(UUID userId, Pageable pageable);
    Optional<Child> findByIdAndUserIdAndActiveTrue(UUID id, UUID userId);
    boolean existsByIdAndUserIdAndActiveTrue(UUID id, UUID userId);
}
