package com.lucas.vacinakids.child.service;

import com.lucas.vacinakids.child.dto.ChildCreateRequest;
import com.lucas.vacinakids.child.dto.ChildResponse;
import com.lucas.vacinakids.child.dto.ChildUpdateRequest;
import com.lucas.vacinakids.child.entity.Child;
import com.lucas.vacinakids.child.repository.ChildRepository;
import com.lucas.vacinakids.shared.exception.NotFoundException;
import com.lucas.vacinakids.user.entity.User;
import com.lucas.vacinakids.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.lucas.vacinakids.vaccination.service.VaccinationScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final UserRepository userRepository;
    private final VaccinationScheduleService scheduleService;

    public ChildService(ChildRepository childRepository, UserRepository userRepository, VaccinationScheduleService scheduleService) {
        this.childRepository = childRepository;
        this.userRepository = userRepository;
        this.scheduleService = scheduleService;
    }

    @Transactional
    public ChildResponse createChild(UUID userId, ChildCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Child child = new Child(
                user,
                request.name().trim(),
                request.birthDate(),
                request.responsibleName().trim(),
                request.notes() != null ? request.notes().trim() : null
        );

        Child savedChild = childRepository.save(child);
        scheduleService.generateScheduleForChild(savedChild);
        return mapToResponse(savedChild);
    }

    @Transactional(readOnly = true)
    public Page<ChildResponse> listChildren(UUID userId, Pageable pageable) {
        return childRepository.findByUserIdAndActiveTrue(userId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public ChildResponse getChildById(UUID userId, UUID childId) {
        Child child = childRepository.findByIdAndUserIdAndActiveTrue(childId, userId)
                .orElseThrow(() -> new NotFoundException("Child not found or access denied"));
        return mapToResponse(child);
    }

    @Transactional
    public ChildResponse updateChild(UUID userId, UUID childId, ChildUpdateRequest request) {
        Child child = childRepository.findByIdAndUserIdAndActiveTrue(childId, userId)
                .orElseThrow(() -> new NotFoundException("Child not found or access denied"));

        child.setName(request.name().trim());
        child.setBirthDate(request.birthDate());
        child.setResponsibleName(request.responsibleName().trim());
        child.setNotes(request.notes() != null ? request.notes().trim() : null);

        Child updatedChild = childRepository.save(child);
        return mapToResponse(updatedChild);
    }

    @Transactional
    public void deleteChild(UUID userId, UUID childId) {
        Child child = childRepository.findByIdAndUserIdAndActiveTrue(childId, userId)
                .orElseThrow(() -> new NotFoundException("Child not found or access denied"));

        child.setActive(false);
        childRepository.save(child);
    }

    private ChildResponse mapToResponse(Child child) {
        return new ChildResponse(
                child.getId(),
                child.getName(),
                child.getBirthDate(),
                child.getResponsibleName(),
                child.getNotes(),
                child.getCreatedAt(),
                child.getUpdatedAt()
        );
    }
}
