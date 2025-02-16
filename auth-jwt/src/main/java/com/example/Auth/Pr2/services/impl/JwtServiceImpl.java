package com.example.Auth.Pr2.services.impl;

import com.example.Auth.Pr2.commons.dtos.TokenResponse;
import com.example.Auth.Pr2.commons.entities.UserModel;
import com.example.Auth.Pr2.services.JwtService;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY = "mysecret";

    @Override
    public TokenResponse generateToken(UserModel user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name()); // Agregar rol al token

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return new TokenResponse(token);
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public boolean isExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    @Override
    public Integer extractUserId(String token) {
        return Integer.parseInt(getClaims(token).getSubject());
    }
}
