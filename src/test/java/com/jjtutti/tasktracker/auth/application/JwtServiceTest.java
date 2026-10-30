package com.jjtutti.tasktracker.auth.application;

import com.jjtutti.tasktracker.auth.config.JwtProperties;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    final String testSecret = "0123456789abcdef0123456789abcdef";
    final String otherSecret = "fedcba9876543210fedcba9876543210";
    final JwtProperties jwtProperties = new JwtProperties(testSecret, Duration.ofHours(1));
    final JwtService jwtService = new JwtService(jwtProperties);

    @Test
    public void generateToken_returnsToken() {
        Long userId = 1L;
        String token = jwtService.generateToken(userId, "test@mail.com");
        var extracted = jwtService.extractUserId(token);
        assertThat(extracted).isNotEmpty();
        assertThat(extracted.get()).isEqualTo(userId);
    }

    @Test
    public void extractUserId_garbageToken_returnsEmpty() {
        assertThat(jwtService.extractUserId("not-a-jwt")).isEmpty();
    }

    @Test
    public void extractUserId_expiredToken_returnsEmpty() throws InterruptedException {
        JwtProperties shortLived = new JwtProperties(testSecret, Duration.ofMillis(-1000));
        JwtService otherJwtService = new JwtService(shortLived);
        String token = otherJwtService.generateToken(1L, "test@mail.com");
        assertThat(otherJwtService.extractUserId(token)).isEmpty();
    }

    @Test
    public void extractUserId_wrongToken_returnsEmpty() {
        JwtProperties otherKey = new JwtProperties(otherSecret, Duration.ofHours(1));
        JwtService otherJwtService = new JwtService(otherKey);
        String otherToken = otherJwtService.generateToken(1L, "test@mail.com");
        assertThat(jwtService.extractUserId(otherToken)).isEmpty();
    }

    @Test
    public void extractUserId_nullToken_returnsEmpty() {
        assertThat(jwtService.extractUserId(null)).isEmpty();
    }
}