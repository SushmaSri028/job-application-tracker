package com.sushma.jobtracker.repository;

import com.sushma.jobtracker.entity.Application;
import com.sushma.jobtracker.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByStatus(ApplicationStatus status);

    List<Application> findByCompanyContainingIgnoreCase(String company);
}