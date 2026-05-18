package com.sushma.jobtracker.repository;

import com.sushma.jobtracker.entity.Application;
import com.sushma.jobtracker.entity.ApplicationStatus;
import com.sushma.jobtracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByUser(User user);

    Optional<Application> findByIdAndUser(Long id, User user);

    List<Application> findByUserAndStatus(User user, ApplicationStatus status);

    List<Application> findByUserAndCompanyContainingIgnoreCase(User user, String company);
}