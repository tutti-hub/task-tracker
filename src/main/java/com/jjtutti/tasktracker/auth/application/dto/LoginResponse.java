package com.jjtutti.tasktracker.auth.application.dto;

public record LoginResponse(String accessToken, String tokenType, long expiresInSec) {
}
