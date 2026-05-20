package com.sushma.jobtracker.service;

import com.sushma.jobtracker.dto.ApplicationRequest;
import com.sushma.jobtracker.entity.Application;
import com.sushma.jobtracker.entity.ApplicationStatus;
import com.sushma.jobtracker.entity.User;
import com.sushma.jobtracker.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.sushma.jobtracker.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ApplicationService")
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository repo;

    @InjectMocks
    private ApplicationService service;

    private User testUser;
    private Application testApp;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("sushma@test.com")
                .fullName("Sushma")
                .build();

        testApp = Application.builder()
                .id(10L)
                .company("Stripe")
                .role("Backend Engineer")
                .status(ApplicationStatus.APPLIED)
                .appliedDate(LocalDate.now())
                .user(testUser)
                .build();
    }

    @Test
    @DisplayName("getAll returns only the user's applications")
    void getAll_returnsOnlyUserApplications() {
        when(repo.findByUser(testUser)).thenReturn(List.of(testApp));

        List<Application> result = service.getAll(testUser);

        assertEquals(1, result.size());
        assertEquals("Stripe", result.get(0).getCompany());
        verify(repo).findByUser(testUser);
    }

    @Test
    @DisplayName("getAll returns empty list when user has no applications")
    void getAll_emptyListWhenNoApplications() {
        when(repo.findByUser(testUser)).thenReturn(List.of());

        List<Application> result = service.getAll(testUser);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getById returns application when found and user owns it")
    void getById_returnsApplicationWhenFound() {
        when(repo.findByIdAndUser(10L, testUser)).thenReturn(Optional.of(testApp));

        Application result = service.getById(10L, testUser);

        assertNotNull(result);
        assertEquals("Stripe", result.getCompany());
    }

    @Test
    @DisplayName("getById throws when application not found")
    void getById_throwsWhenNotFound() {
        when(repo.findByIdAndUser(99L, testUser)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> service.getById(99L, testUser)
        );
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    @DisplayName("create saves a new application with the current user attached")
    void create_savesWithUser() {
        ApplicationRequest req = new ApplicationRequest();
        req.setCompany("Google");
        req.setRole("SWE");
        req.setStatus(ApplicationStatus.APPLIED);
        req.setAppliedDate(LocalDate.now());

        Application saved = Application.builder()
                .id(20L)
                .company("Google")
                .role("SWE")
                .user(testUser)
                .build();

        when(repo.save(any(Application.class))).thenReturn(saved);

        Application result = service.create(req, testUser);

        assertEquals("Google", result.getCompany());
        assertEquals(testUser, result.getUser());
        verify(repo).save(any(Application.class));
    }

    @Test
    @DisplayName("update modifies fields and saves")
    void update_modifiesAndSaves() {
        ApplicationRequest req = new ApplicationRequest();
        req.setCompany("Stripe");
        req.setRole("Senior Backend Engineer");
        req.setStatus(ApplicationStatus.INTERVIEW);
        req.setAppliedDate(LocalDate.now());

        when(repo.findByIdAndUser(10L, testUser)).thenReturn(Optional.of(testApp));
        when(repo.save(any(Application.class))).thenReturn(testApp);

        Application result = service.update(10L, req, testUser);

        assertEquals("Senior Backend Engineer", result.getRole());
        assertEquals(ApplicationStatus.INTERVIEW, result.getStatus());
        verify(repo).save(testApp);
    }

    @Test
    @DisplayName("update throws when application doesn't belong to user")
    void update_throwsWhenNotOwned() {
        ApplicationRequest req = new ApplicationRequest();
        req.setCompany("X");
        req.setRole("Y");

        when(repo.findByIdAndUser(99L, testUser)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.update(99L, req, testUser));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("delete removes application when user owns it")
    void delete_removesOwnedApplication() {
        when(repo.findByIdAndUser(10L, testUser)).thenReturn(Optional.of(testApp));

        service.delete(10L, testUser);

        verify(repo).delete(testApp);
    }

    @Test
    @DisplayName("delete throws when application not found")
    void delete_throwsWhenNotFound() {
        when(repo.findByIdAndUser(99L, testUser)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getById(99L, testUser));
        verify(repo, never()).delete(any());
    }
}