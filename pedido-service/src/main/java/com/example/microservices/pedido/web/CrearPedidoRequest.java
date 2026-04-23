package com.example.microservices.pedido.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearPedidoRequest(
        @NotNull Long usuarioId,
        @NotBlank String descripcion) {
}
