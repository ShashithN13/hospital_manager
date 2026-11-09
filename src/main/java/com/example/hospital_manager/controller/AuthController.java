package com.example.hospitalmanager.controller;

import com.example.hospitalmanager.dto.*;
import com.example.hospitalmanager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { this.service=service; }
    @PostMapping("/register") public UserResponse register(@Valid @RequestBody RegisterRequest req) { return service.register(req); }
    @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest req) { return service.login(req); }
    @GetMapping("/me") public UserResponse me(Authentication auth) { return service.me(auth.getName()); }
}
