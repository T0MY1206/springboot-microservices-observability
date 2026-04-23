package com.example.microservices.pedido.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class PedidoRepositoryTest {

    @Autowired
    PedidoRepository pedidoRepository;

    @Test
    void guardaPedido() {
        Pedido p = new Pedido();
        p.setUsuarioId(1L);
        p.setDescripcion("Pedido test");
        p.setCreadoEn(Instant.now());
        pedidoRepository.save(p);
        assertThat(pedidoRepository.findAll()).hasSize(1);
    }
}
