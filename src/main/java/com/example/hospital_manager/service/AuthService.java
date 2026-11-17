package com.example.hospital_manager.service;

import com.example.hospital_manager.dto.*;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.*;
import com.example.hospital_manager.repository.UserRepository;
import com.example.hospital_manager.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, AuthenticationManager authManager, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwt = jwt;
    }

    public UserResponse register(RegisterRequest req) {
        if (users.existsByUsername(req.username())) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists");
        }
        AppUser user = users.save(AppUser.builder().username(req.username()).password(encoder.encode(req.password()))
          .fullName(req.fullName()).role(req.role()).build());
        return toResponse(user);
    }

    public AuthResponse login(LoginRequest req) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        } catch (AuthenticationException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        AppUser user = users.findByUsername(req.username()).orElseThrow();
        return new AuthResponse(jwt.generateToken(user), "Bearer", jwt.getExpirationMs(), user.getUsername(), user.getFullName(), user.getRole().name());
    }

    public UserResponse me(String username) {
        return toResponse(users.findByUsername(username).orElseThrow());
    }

    public List<UserResponse> listUsers() {
        return users.findAll().stream().map(this::toResponse).toList();
    }

    public List<UserResponse> doctors() {
        return users.findByRole(Role.DOCTOR).stream().map(this::toResponse).toList();
    }

    private UserResponse toResponse(AppUser u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getFullName(), u.getRole().name());
    }
}
