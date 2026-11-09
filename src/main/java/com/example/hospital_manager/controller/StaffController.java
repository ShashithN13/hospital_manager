package com.example.hospitalmanager.controller;

import com.example.hospitalmanager.dto.UserResponse;
import com.example.hospitalmanager.service.AuthService;
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
