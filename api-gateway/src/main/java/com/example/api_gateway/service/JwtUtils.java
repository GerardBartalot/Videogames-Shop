package com.example.api_gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtils {
    private final String secretKey = "aP9fLd8JkT5rX2ZvQh4mCwByNs7GtYpWqR3EjKuVoMx6HcAbD1nLf8PtZy4XqR9mA5sR6dK7wM4NvZ2pgsja78SDjakskdjs9872SjsDjdjdj8";

    // Generar una clave segura basada en el secretKey
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extraer Claims del Token
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extraer un dato específico del Token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Validar si el Token está expirado
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Validar Token
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // Extraer User ID del Token
    public Integer extractUserId(String token) {
        try {
            return Integer.parseInt(extractClaim(token, Claims::getSubject));
        } catch (Exception e) {
            return null;
        }
    }

    // Extraer Role del Token
    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
}
