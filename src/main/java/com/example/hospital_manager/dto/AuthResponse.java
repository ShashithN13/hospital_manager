package com.example.hospital_manager.dto;
public record AuthResponse(String token, String tokenType, long expiresIn, String username, String fullName, String role) {}
