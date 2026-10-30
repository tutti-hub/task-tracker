package com.jjtutti.tasktracker.auth.application;

import com.jjtutti.tasktracker.auth.application.dto.LoginRequest;
import com.jjtutti.tasktracker.auth.application.dto.LoginResponse;
import com.jjtutti.tasktracker.auth.application.dto.RegisterRequest;
import com.jjtutti.tasktracker.auth.application.dto.UserResponse;
import com.jjtutti.tasktracker.auth.config.JwtProperties;
import com.jjtutti.tasktracker.auth.infra.exception.EmailAlreadyExistsException;
import com.jjtutti.tasktracker.auth.infra.exception.InvalidCredentialsException;
import com.jjtutti.tasktracker.user.User;
import com.jjtutti.tasktracker.user.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    @Transactional
    public UserResponse register(RegisterRequest req) {
        Objects.requireNonNull(req, "Register request can not be null");

        if(userRepository.existsByEmail(req.email())){
            throw  new EmailAlreadyExistsException(req.email());
        }

        User user = new User(req.email(), passwordEncoder.encode(req.password()));

        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            throw new EmailAlreadyExistsException(req.email());        }

        return new UserResponse(user.getId(), user.getEmail(), user.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) throws NoSuchAlgorithmException {
        User user = userRepository.findByEmail(request.email()).orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return new LoginResponse(token, "Bearer", jwtProperties.expiration().getSeconds());
    }

}
