package com.example.hospitalmanager.controller;

import com.example.hospitalmanager.dto.*;
import com.example.hospitalmanager.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService service;
    public AppointmentController(AppointmentService service) { this.service=service; }
    @PostMapping @PreAuthorize("hasAnyRole('PATIENT','ADMIN','RECEPTIONIST')") public AppointmentResponse book(@Valid @RequestBody AppointmentRequest req) { return service.book(req); }
    @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<AppointmentResponse> all() { return service.all(); }
    @GetMapping("/mine") @PreAuthorize("hasRole('DOCTOR')") public List<AppointmentResponse> mine(Authentication auth) { return service.byDoctor(auth.getName()); }
    @GetMapping("/patient/{id}") @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')") public List<AppointmentResponse> patient(@PathVariable Long id) { return service.byPatient(id); }
    @PatchMapping("/{id}/status") @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')") public AppointmentResponse status(@PathVariable Long id,@Valid @RequestBody StatusRequest req) { return service.updateStatus(id,req); }
}
