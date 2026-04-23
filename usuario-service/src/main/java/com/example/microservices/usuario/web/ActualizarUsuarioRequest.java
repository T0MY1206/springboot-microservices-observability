package com.example.microservices.usuario.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ActualizarUsuarioRequest(
        @NotBlank String nombre,
        @NotBlank @Email String email) {
}
