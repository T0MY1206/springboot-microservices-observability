package com.example.microservices.pedido.security;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RoleAuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            return true;
        }

        String role = request.getHeader("X-User-Role");
        if (role == null || role.isBlank()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token o rol no presente");
            return false;
        }

        if (HttpMethod.GET.matches(request.getMethod())) {
            if ("USER".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role)) {
                return true;
            }
            response.sendError(HttpStatus.FORBIDDEN.value(), "Rol no autorizado");
            return false;
        }

        if ("ADMIN".equalsIgnoreCase(role)) {
            return true;
        }

        response.sendError(HttpStatus.FORBIDDEN.value(), "Solo ADMIN puede crear pedidos");
        return false;
    }
}
