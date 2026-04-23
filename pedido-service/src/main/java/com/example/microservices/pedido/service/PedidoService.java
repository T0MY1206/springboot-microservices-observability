package com.example.microservices.pedido.service;

import java.time.Instant;
import java.util.List;

import com.example.microservices.pedido.client.UsuarioClient;
import com.example.microservices.pedido.domain.Pedido;
import com.example.microservices.pedido.domain.PedidoRepository;
import com.example.microservices.pedido.web.CrearPedidoRequest;
import com.example.microservices.pedido.web.PedidoResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioClient usuarioClient;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioClient usuarioClient) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioClient = usuarioClient;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse obtener(Long id) {
        return pedidoRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));
    }

    @Transactional
    public PedidoResponse crear(CrearPedidoRequest request) {
        usuarioClient.ensureUsuarioExists(request.usuarioId());
        Pedido p = new Pedido();
        p.setUsuarioId(request.usuarioId());
        p.setDescripcion(request.descripcion());
        p.setCreadoEn(Instant.now());
        return toResponse(pedidoRepository.save(p));
    }

    private PedidoResponse toResponse(Pedido p) {
        return new PedidoResponse(p.getId(), p.getUsuarioId(), p.getDescripcion(), p.getCreadoEn());
    }
}
