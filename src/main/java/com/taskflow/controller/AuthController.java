package com.taskflow.controller;

import com.taskflow.model.User;
import com.taskflow.service.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public record RegisterReq(@NotBlank String name, @Email @NotBlank String email, @Size(min = 6) String password) {}
    public record LoginReq(@NotBlank String email, @NotBlank String password) {}

    private final AuthService auth;
    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/register")
    public Map<String, Object> register(@Valid @RequestBody RegisterReq r) {
        return response(auth.register(r.name(), r.email(), r.password()));
    }

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginReq r) {
        return response(auth.login(r.email(), r.password()));
    }

    private Map<String, Object> response(User u) {
        return Map.of("token", u.getToken(), "name", u.getName(), "email", u.getEmail());
    }
}
