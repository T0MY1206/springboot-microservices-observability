package com.example.microservices.pedido.web;

import java.time.Instant;

public record PedidoResponse(Long id, Long usuarioId, String descripcion, Instant creadoEn) {
}
