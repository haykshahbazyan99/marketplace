package io.webz.marketplace.services;

import io.webz.marketplace.dto.AuthenticationRequestDTO;
import io.webz.marketplace.dto.AuthenticationResponseDTO;
import io.webz.marketplace.dto.UserRegisterDTO;
import io.webz.marketplace.models.Role;
import io.webz.marketplace.models.User;
import io.webz.marketplace.repositories.UserRepository;
import io.webz.marketplace.security.CustomUserDetails;
import io.webz.marketplace.security.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                 JWTUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponseDTO register(UserRegisterDTO request) {
        this.userRepository.findByUsername(request.getUsername())
                .ifPresent(existingUser -> {
                    logger.error("Username already taken: {}", request.getUsername());
                    throw new DataIntegrityViolationException("username already taken: add another one or try guessing the password");
                });

        User user = new User();
        user.setFullName(request.getFullName());
        user.setAddress(request.getAddress());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.PUBLISHER);

        userRepository.save(user);
        logger.info("User registered successfully with username: {}", request.getUsername());

        String jwtToken = jwtUtils.generateToken(new CustomUserDetails(user));

        return new AuthenticationResponseDTO(jwtToken);
    }


    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for user: {} due to invalid credentials", request.getUsername());
            throw new BadCredentialsException("Invalid credentials");
        }

        User user = this.userRepository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = this.jwtUtils.generateToken(new CustomUserDetails(user));

        return new AuthenticationResponseDTO(jwtToken);
    }

}
