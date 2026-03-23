package com.buyPhone.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtil {

    private static final long EXPIRATION_TIME = 1000L * 60 * 15; // 15 minutes

    private static final String ISSUER = "buyphone-api";
    private static final String AUDIENCE = "buyphone-client";

    @Value("${jwt.secret.key}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {
        // Ensure key is strong (>= 256 bits)
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate Token
    public String generateToken(String userId, String role) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(userId)
                .issuer(ISSUER)
                .audience().add(AUDIENCE).and()
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key, Jwts.SIG.HS256) // enforce algorithm
                .compact();
    }

    // Validate Token
    public Claims validateToken(String token) {

        try {
            return Jwts.parser()
                    .verifyWith(key) // do NOT trust header
                    .requireIssuer(ISSUER)
                    .requireAudience(AUDIENCE)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid or expired JWT", e);
        }
    }

    // Extract Claim Safely
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(validateToken(token));
    }

    // Extract User ID
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract Role
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Expiry Check
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Final Validation (No UserDetails dependency)
    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}