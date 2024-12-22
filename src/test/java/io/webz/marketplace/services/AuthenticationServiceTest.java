package io.webz.marketplace.services;

import io.webz.marketplace.dto.AuthenticationRequestDTO;
import io.webz.marketplace.dto.AuthenticationResponseDTO;
import io.webz.marketplace.dto.UserRegisterDTO;
import io.webz.marketplace.models.Role;
import io.webz.marketplace.models.User;
import io.webz.marketplace.repositories.UserRepository;
import io.webz.marketplace.security.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTUtils jwtUtils;
    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtils = mock(JWTUtils.class);
        authenticationManager = mock(AuthenticationManager.class);

        authenticationService = new AuthenticationService(userRepository, passwordEncoder, jwtUtils, authenticationManager);
    }


    @Test
    void userSuccessfulRegistrationTest() {
        UserRegisterDTO request = new UserRegisterDTO("test full name", "test address",
                "testUser", "testPassword");

        User user = new User();
        user.setUsername("testUser");
        user.setRole(Role.PUBLISHER);

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtUtils.generateToken(any())).thenReturn("fakeToken");

        AuthenticationResponseDTO response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("fakeToken", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void userSuccessfulAuthenticationTest() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("testuser", "password");
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(any())).thenReturn("fakeToken");

        AuthenticationResponseDTO response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals("fakeToken", response.getToken());
    }


    @Test
    void testAuthenticateFailure() {
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("testuser", "wrongpassword");

        doThrow(new RuntimeException("Invalid username or password"))
                .when(authenticationManager)
                .authenticate(any());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticationService.authenticate(request));
        assertEquals("Invalid username or password", exception.getMessage());
    }

}
