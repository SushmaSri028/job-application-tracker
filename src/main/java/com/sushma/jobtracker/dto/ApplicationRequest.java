package com.sushma.jobtracker.dto;

import com.sushma.jobtracker.entity.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ApplicationRequest {

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Role is required")
    private String role;

    private String location;
    private String jobUrl;
    private String notes;
    private ApplicationStatus status;
    private LocalDate appliedDate;
}