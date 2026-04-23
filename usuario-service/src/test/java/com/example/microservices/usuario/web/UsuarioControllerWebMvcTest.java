package com.example.microservices.usuario.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.microservices.usuario.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UsuarioService usuarioService;

    @Test
    void obtenerDevuelveJson() throws Exception {
        when(usuarioService.obtener(1L)).thenReturn(new UsuarioResponse(1L, "Ana", "ana@example.com"));

        mockMvc.perform(get("/api/usuarios/1")
                        .header("X-User-Role", "USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void crearValida() throws Exception {
        when(usuarioService.crear(any(CrearUsuarioRequest.class)))
                .thenReturn(new UsuarioResponse(1L, "Ana", "ana@example.com"));

        mockMvc.perform(post("/api/usuarios")
                        .header("X-User-Role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ana\",\"email\":\"ana@example.com\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void actualizar() throws Exception {
        when(usuarioService.actualizar(eq(1L), any(ActualizarUsuarioRequest.class)))
                .thenReturn(new UsuarioResponse(1L, "Ana", "ana@example.com"));

        mockMvc.perform(put("/api/usuarios/1")
                        .header("X-User-Role", "ADMIN")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Ana\",\"email\":\"ana@example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminar() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1")
                        .header("X-User-Role", "ADMIN"))
                .andExpect(status().isNoContent());
    }
}
