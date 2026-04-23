package com.example.microservices.pedido.client;

import java.time.Duration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

public class UsuarioClient {

    private final WebClient webClient;
    private final long timeoutMs;

    public UsuarioClient(WebClient webClient, long timeoutMs) {
        this.webClient = webClient;
        this.timeoutMs = timeoutMs;
    }

    @Retry(name = "usuarioService")
    @CircuitBreaker(name = "usuarioService", fallbackMethod = "fallbackEnsureUsuarioExists")
    public void ensureUsuarioExists(Long usuarioId) {
        try {
            webClient.get()
                    .uri("/api/usuarios/{id}", usuarioId)
                    .header("X-Internal-Call", "true")
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, response -> reactor.core.publisher.Mono
                            .error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                                    "Usuario-service devolvio error interno")))
                    .toBodilessEntity()
                    .block(Duration.ofMillis(timeoutMs));
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no existe");
            }
            if (e.getStatusCode().is5xxServerError()) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "Usuario-service no disponible temporalmente");
            }
            throw e;
        } catch (IllegalStateException e) {
            if (e.getMessage() != null && e.getMessage().contains("Timeout on blocking read")) {
                throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT,
                        "Timeout consultando usuario-service");
            }
            throw e;
        }
    }

    @SuppressWarnings("unused")
    private void fallbackEnsureUsuarioExists(Long usuarioId, Throwable throwable) {
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "No fue posible validar el usuario en este momento");
    }
}
