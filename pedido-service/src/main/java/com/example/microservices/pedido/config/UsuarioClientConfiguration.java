package com.example.microservices.pedido.config;

import com.example.microservices.pedido.client.UsuarioClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UsuarioClientConfiguration {

    @Bean
    UsuarioClient usuarioClient(
            @LoadBalanced WebClient.Builder loadBalancedWebClientBuilder,
            @Value("${app.usuario-service.base-url:http://usuario-service}") String baseUrl,
            @Value("${app.usuario-service.timeout-ms:2000}") long timeoutMs) {
        return new UsuarioClient(loadBalancedWebClientBuilder.baseUrl(baseUrl).build(), timeoutMs);
    }
}
