package io.webz.marketplace.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.webz.marketplace.dto.AuthenticationRequestDTO;
import io.webz.marketplace.dto.AuthenticationResponseDTO;
import io.webz.marketplace.dto.UserRegisterDTO;
import io.webz.marketplace.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/register")
    @Operation(summary = "Registration of a new user")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody UserRegisterDTO request) {
        return ResponseEntity.ok(authenticationService.register(request));

    }


    @PostMapping("/authenticate")
    @Operation(summary = "Login and authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}
