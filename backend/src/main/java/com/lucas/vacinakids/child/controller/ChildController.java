package com.lucas.vacinakids.child.controller;

import com.lucas.vacinakids.auth.security.UserDetailsImpl;
import com.lucas.vacinakids.child.dto.ChildCreateRequest;
import com.lucas.vacinakids.child.dto.ChildResponse;
import com.lucas.vacinakids.child.dto.ChildUpdateRequest;
import com.lucas.vacinakids.child.service.ChildService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Tag(name = "Children", description = "Gerenciamento de crianças da família")
@RequestMapping("/api/v1/children")
public class ChildController {

    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChildResponse createChild(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     @Valid @RequestBody ChildCreateRequest request) {
        return childService.createChild(userDetails.getId(), request);
    }

    @GetMapping
    public ResponseEntity<Page<ChildResponse>> listChildren(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            Pageable pageable) {
        return ResponseEntity.ok(childService.listChildren(userDetails.getId(), pageable));
    }

    @GetMapping("/{childId}")
    @Operation(summary = "Buscar uma criança específica por ID")
    public ResponseEntity<ChildResponse> getChildById(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable UUID childId) {
        return ResponseEntity.ok(childService.getChildById(userDetails.getId(), childId));
    }

    @PutMapping("/{childId}")
    @Operation(summary = "Atualizar dados de uma criança")
    public ResponseEntity<ChildResponse> updateChild(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable UUID childId,
                                                     @Valid @RequestBody ChildUpdateRequest request) {
        return ResponseEntity.ok(childService.updateChild(userDetails.getId(), childId, request));
    }

    @DeleteMapping("/{childId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChild(@AuthenticationPrincipal UserDetailsImpl userDetails,
                            @PathVariable UUID childId) {
        childService.deleteChild(userDetails.getId(), childId);
    }
}
