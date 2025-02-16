package com.example.api_gateway.service;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GatewayFilter {

    private final JwtUtils jwtUtils;
    private final RouterValidator routerValidator;

    public AuthenticationFilter(JwtUtils jwtUtils, RouterValidator routerValidator) {
        this.jwtUtils = jwtUtils;
        this.routerValidator = routerValidator;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            String token = request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0).replace("Bearer ", "");

            try {
                jwtUtils.validateToken(token);
            } catch (Exception e) {
                return onError(exchange, "Invalid or Expired Token", HttpStatus.UNAUTHORIZED);
            }

            Claims claims = jwtUtils.extractAllClaims(token);
            String role = claims.get("role", String.class);

            // Protección de rutas según el rol del usuario
            String path = request.getURI().getPath();

            if (path.startsWith("/v1/auth/users") && !"ADMIN".equals(role)) {
                return onError(exchange, "Access Denied", HttpStatus.FORBIDDEN);
            }

            if (path.startsWith("/v1/games") && !"ADMIN".equals(role)) {
                return onError(exchange, "Access Denied", HttpStatus.FORBIDDEN);
            }

            if (path.startsWith("/v1/games/buy") && !"USER".equals(role)) {
                return onError(exchange, "Access Denied", HttpStatus.FORBIDDEN);
            }

            return chain.filter(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
}
