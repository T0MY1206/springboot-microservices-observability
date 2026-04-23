package com.example.microservices.pedido.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

class UsuarioClientWireMockTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance().options(
            com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig().dynamicPort()).build();

    @Test
    void ensureUsuarioExistsCuando404() {
        wireMock.stubFor(get(urlEqualTo("/api/usuarios/99"))
                .willReturn(aResponse().withStatus(404)));

        UsuarioClient client = new UsuarioClient(
                WebClient.builder().baseUrl(wireMock.getRuntimeInfo().getHttpBaseUrl()).build(),
                2000);

        assertThatThrownBy(() -> client.ensureUsuarioExists(99L))
                .isInstanceOf(ResponseStatusException.class)
                .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode().value()).isEqualTo(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void ensureUsuarioExistsCuando200() {
        wireMock.stubFor(get(urlEqualTo("/api/usuarios/1"))
                .willReturn(aResponse().withStatus(200)));

        UsuarioClient client = new UsuarioClient(
                WebClient.builder().baseUrl(wireMock.getRuntimeInfo().getHttpBaseUrl()).build(),
                2000);

        client.ensureUsuarioExists(1L);
    }
}
