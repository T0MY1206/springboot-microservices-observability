package com.example.microservices.pedido.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import com.example.microservices.pedido.service.PedidoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PedidoController.class)
class PedidoControllerWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PedidoService pedidoService;

    @Test
    void obtener() throws Exception {
        when(pedidoService.obtener(1L)).thenReturn(
                new PedidoResponse(1L, 2L, "desc", Instant.parse("2026-01-01T00:00:00Z")));

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("desc"));
    }

    @Test
    void crear() throws Exception {
        when(pedidoService.crear(any(CrearPedidoRequest.class)))
                .thenReturn(new PedidoResponse(1L, 2L, "nuevo", Instant.parse("2026-01-01T00:00:00Z")));

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuarioId\":2,\"descripcion\":\"nuevo\"}"))
                .andExpect(status().isCreated());
    }
}
