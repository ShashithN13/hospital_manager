package com.example.hospital_manager.controller;

import com.example.hospital_manager.dto.UserResponse;
import com.example.hospital_manager.service.AuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api")
public class StaffController {
    private final AuthService service;
    public StaffController(AuthService service) { this.service=service; }
    @GetMapping("/users") @PreAuthorize("hasRole('ADMIN')") public List<UserResponse> users() { return service.listUsers(); }
    @GetMapping("/doctors") @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','PATIENT','NURSE','RECEPTIONIST')") public List<UserResponse> doctors() { return service.doctors(); }
}
