package com.fitly.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Base64;

import java.security.Key;


@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-in-ms}")
    private long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        System.out.println("Initializing JWT with secret: " + secret);

        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(decodedKey);

        System.out.println("Key algorithm: " + key.getAlgorithm());
        System.out.println("Key format: " + key.getFormat());
        System.out.println("Key encoded: " + Base64.getEncoder().encodeToString(key.getEncoded()));
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String subject = claims.getSubject();
        if (subject == null) {
            throw new RuntimeException("Token neobsahuje subject (userId)");
        }
        return Long.valueOf(subject);
    }
}
