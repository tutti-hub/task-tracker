package com.jjtutti.tasktracker.auth.application.dto;

import java.time.OffsetDateTime;

public record UserResponse(Long id, String email, OffsetDateTime createdAt) {
}
