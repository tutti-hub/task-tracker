package com.jjtutti.tasktracker.auth.infra.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email " + email + " already exists");
    }
}
