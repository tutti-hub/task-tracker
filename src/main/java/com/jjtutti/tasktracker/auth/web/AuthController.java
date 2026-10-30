package com.jjtutti.tasktracker.auth.web;

import com.jjtutti.tasktracker.auth.application.AuthService;
import com.jjtutti.tasktracker.auth.application.dto.LoginRequest;
import com.jjtutti.tasktracker.auth.application.dto.LoginResponse;
import com.jjtutti.tasktracker.auth.application.dto.RegisterRequest;
import com.jjtutti.tasktracker.auth.application.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid  @RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) throws NoSuchAlgorithmException {
        return authService.login(req);
    }
}
