package com.jjtutti.tasktracker.user;

import jakarta.persistence.*;
import lombok.Getter;
import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    protected User(){}

    public User(String email, String passwordHash) {
        setEmail(email);
        setPasswordHash(passwordHash);
    }

    private void setEmail(String email) {
        Objects.requireNonNull(email, "email can not be null");
        this.email = email;
    }

    private void setPasswordHash(String passwordHash) {
        Objects.requireNonNull(passwordHash, "Password hash can not be null");
        this.passwordHash = passwordHash;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ( !(obj instanceof User other)) return false;
        if (id == null || other.id == null) return false;
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "{id=" + id +
                ", email=" + email +
                ", createdAt=" + createdAt + "}";
    }
}
