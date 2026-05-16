package com.sushma.jobtracker.service;

import com.sushma.jobtracker.dto.ApplicationRequest;
import com.sushma.jobtracker.entity.Application;
import com.sushma.jobtracker.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository repo;

    public List<Application> getAll() {
        return repo.findAll();
    }

    public Application getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found: " + id));
    }

    public Application create(ApplicationRequest req) {
        Application app = Application.builder()
                .company(req.getCompany())
                .role(req.getRole())
                .location(req.getLocation())
                .jobUrl(req.getJobUrl())
                .notes(req.getNotes())
                .status(req.getStatus())
                .appliedDate(req.getAppliedDate())
                .build();
        return repo.save(app);
    }

    public Application update(Long id, ApplicationRequest req) {
        Application app = getById(id);
        app.setCompany(req.getCompany());
        app.setRole(req.getRole());
        app.setLocation(req.getLocation());
        app.setJobUrl(req.getJobUrl());
        app.setNotes(req.getNotes());
        if (req.getStatus() != null) {
            app.setStatus(req.getStatus());
        }
        app.setAppliedDate(req.getAppliedDate());
        return repo.save(app);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Application not found: " + id);
        }
        repo.deleteById(id);
    }
}