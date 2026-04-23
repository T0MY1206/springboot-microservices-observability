package com.example.microservices.pedido.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.time.Instant;

import com.example.microservices.pedido.client.UsuarioClient;
import com.example.microservices.pedido.domain.Pedido;
import com.example.microservices.pedido.domain.PedidoRepository;
import com.example.microservices.pedido.web.CrearPedidoRequest;
import com.example.microservices.pedido.web.PedidoResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    UsuarioClient usuarioClient;

    @InjectMocks
    PedidoService pedidoService;

    @Test
    void crearValidaUsuario() {
        doNothing().when(usuarioClient).ensureUsuarioExists(5L);
        Pedido guardado = new Pedido();
        guardado.setId(1L);
        guardado.setUsuarioId(5L);
        guardado.setDescripcion("x");
        guardado.setCreadoEn(Instant.parse("2026-01-01T00:00:00Z"));
        org.mockito.Mockito.when(pedidoRepository.save(any(Pedido.class))).thenReturn(guardado);

        PedidoResponse r = pedidoService.crear(new CrearPedidoRequest(5L, "x"));

        assertThat(r.id()).isEqualTo(1L);
        assertThat(r.usuarioId()).isEqualTo(5L);
        verify(usuarioClient).ensureUsuarioExists(5L);
    }

    @Test
    void crearFallaSiUsuarioNoExiste() {
        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "El usuario no existe"))
                .when(usuarioClient).ensureUsuarioExists(9L);

        assertThatThrownBy(() -> pedidoService.crear(new CrearPedidoRequest(9L, "x")))
                .isInstanceOf(ResponseStatusException.class);
    }
}
