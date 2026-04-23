package com.example.microservices.usuario.service;

import java.util.List;

import com.example.microservices.usuario.domain.Usuario;
import com.example.microservices.usuario.domain.UsuarioRepository;
import com.example.microservices.usuario.web.ActualizarUsuarioRequest;
import com.example.microservices.usuario.web.CrearUsuarioRequest;
import com.example.microservices.usuario.web.UsuarioResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(Long id) {
        return usuarioRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @Transactional
    public UsuarioResponse crear(CrearUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya registrado");
        }
        Usuario u = new Usuario();
        u.setNombre(request.nombre());
        u.setEmail(request.email());
        return toResponse(usuarioRepository.save(u));
    }

    @Transactional
    public UsuarioResponse actualizar(Long id, ActualizarUsuarioRequest request) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuarioRepository.findByEmail(request.email())
                .filter(other -> !other.getId().equals(id))
                .ifPresent(x -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya registrado");
                });
        u.setNombre(request.nombre());
        u.setEmail(request.email());
        return toResponse(usuarioRepository.save(u));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail());
    }
}
