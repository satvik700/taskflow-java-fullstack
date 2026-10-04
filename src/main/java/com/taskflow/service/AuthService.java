package com.taskflow.service;

import com.taskflow.model.User;
import com.taskflow.repo.UserRepository;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository users) { this.users = users; }

    public User register(String name, String email, String password) {
        if (users.findByEmail(email).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(password));
        u.setToken(UUID.randomUUID().toString());
        return users.save(u);
    }

    public User login(String email, String password) {
        User u = users.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!encoder.matches(password, u.getPasswordHash()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        u.setToken(UUID.randomUUID().toString());
        return users.save(u);
    }

    /** Resolves the logged-in user from an "Authorization: Bearer <token>" header. */
    public User require(String header) {
        if (header == null || !header.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        return users.findByToken(header.substring(7))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));
    }
}
