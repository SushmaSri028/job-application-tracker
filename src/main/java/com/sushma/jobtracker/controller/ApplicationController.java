package com.sushma.jobtracker.controller;

import com.sushma.jobtracker.dto.ApplicationRequest;
import com.sushma.jobtracker.entity.Application;
import com.sushma.jobtracker.entity.User;
import com.sushma.jobtracker.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ApplicationController {

    private final ApplicationService service;

    @GetMapping
    public List<Application> getAll(@AuthenticationPrincipal User currentUser) {
        return service.getAll(currentUser);
    }

    @GetMapping("/{id}")
    public Application getOne(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        return service.getById(id, currentUser);
    }

    @PostMapping
    public ResponseEntity<Application> create(
            @Valid @RequestBody ApplicationRequest req,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.status(201).body(service.create(req, currentUser));
    }

    @PutMapping("/{id}")
    public Application update(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationRequest req,
            @AuthenticationPrincipal User currentUser
    ) {
        return service.update(id, req, currentUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        service.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}