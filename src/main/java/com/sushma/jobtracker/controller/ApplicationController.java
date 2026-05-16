package com.sushma.jobtracker.controller;

import com.sushma.jobtracker.dto.ApplicationRequest;
import com.sushma.jobtracker.entity.Application;
import com.sushma.jobtracker.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ApplicationController {

    private final ApplicationService service;

    @GetMapping
    public List<Application> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Application getOne(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<Application> create(@Valid @RequestBody ApplicationRequest req) {
        Application created = service.create(req);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    public Application update(@PathVariable Long id, @Valid @RequestBody ApplicationRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}