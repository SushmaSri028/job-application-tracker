package com.sushma.jobtracker.service;

import com.sushma.jobtracker.dto.AuthResponse;
import com.sushma.jobtracker.dto.LoginRequest;
import com.sushma.jobtracker.dto.RegisterRequest;
import com.sushma.jobtracker.entity.User;
import com.sushma.jobtracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService")
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("register creates a new user with hashed password and returns token")
    void register_createsUserAndReturnsToken() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("new@test.com");
        req.setPassword("plainpass123");
        req.setFullName("New User");

        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(passwordEncoder.encode("plainpass123")).thenReturn("HASHED");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtService.generateToken(any(User.class))).thenReturn("fake.jwt.token");

        AuthResponse response = authService.register(req);

        assertEquals("fake.jwt.token", response.getToken());
        assertEquals("new@test.com", response.getEmail());
        assertEquals("New User", response.getFullName());
        verify(passwordEncoder).encode("plainpass123");  // password was hashed
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("register throws when email already in use")
    void register_throwsWhenEmailExists() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("existing@test.com");
        req.setPassword("pass");
        req.setFullName("X");

        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> authService.register(req)
        );
        assertTrue(ex.getMessage().toLowerCase().contains("email"));

        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    @DisplayName("login returns a token for valid credentials")
    void login_validCredentialsReturnsToken() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@test.com");
        req.setPassword("rightpass");

        User user = User.builder()
                .id(5L)
                .email("user@test.com")
                .fullName("Test User")
                .password("hashedpass")
                .build();

        // authenticate doesn't throw → credentials are valid
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("login.jwt.token");

        AuthResponse response = authService.login(req);

        assertEquals("login.jwt.token", response.getToken());
        assertEquals("user@test.com", response.getEmail());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    @DisplayName("login throws BadCredentialsException for wrong password")
    void login_wrongPasswordThrows() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@test.com");
        req.setPassword("wrongpass");

        // The authenticationManager rejects bad credentials
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        assertThrows(BadCredentialsException.class, () -> authService.login(req));

        // we never reached the token-generation step
        verify(jwtService, never()).generateToken(any(User.class));
    }
}