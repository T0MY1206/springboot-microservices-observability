package com.example.microservices.gateway.web;

import com.example.microservices.gateway.security.JwtService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthTokenResponse token(@Valid @RequestBody AuthTokenRequest request) {
        String token = jwtService.generateToken(request.username(), request.role());
        return new AuthTokenResponse(token, "Bearer");
    }

    public record AuthTokenRequest(@NotBlank String username, @NotBlank String role) {
    }

    public record AuthTokenResponse(String accessToken, String tokenType) {
    }
}
