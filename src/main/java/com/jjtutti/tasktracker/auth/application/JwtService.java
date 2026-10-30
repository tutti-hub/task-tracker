package com.jjtutti.tasktracker.auth.application;

import com.jjtutti.tasktracker.auth.config.JwtProperties;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    private final SecretKey key;

    public JwtService(JwtProperties jwtProperties) {
        Objects.requireNonNull(jwtProperties, "jwtProperties cannot be null");
        Objects.requireNonNull(jwtProperties.secret(), "jwt.secret must be configured");
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, String email) {

        Objects.requireNonNull(userId, "User id can not be null");
        Objects.requireNonNull(email, "Email can not be null");

        Instant now = Instant.now();

        Date exp = Date.from(now.plus(jwtProperties.expiration()));

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .expiration(exp)
                .subject(userId.toString())
                .issuedAt(Date.from(now))
                .signWith(key, Jwts.SIG.HS256)
                .claim("email", email)
                .compact();
    }

    public Optional<Long> extractUserId(String token) {
        try {
            String subject = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
            return Optional.of(Long.parseLong(subject));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

}
