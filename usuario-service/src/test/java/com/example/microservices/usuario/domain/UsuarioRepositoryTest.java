package com.example.microservices.usuario.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    void guardaYBuscaPorEmail() {
        Usuario u = new Usuario();
        u.setNombre("Test");
        u.setEmail("t@example.com");
        usuarioRepository.save(u);
        assertThat(usuarioRepository.findByEmail("t@example.com")).isPresent();
    }
}
