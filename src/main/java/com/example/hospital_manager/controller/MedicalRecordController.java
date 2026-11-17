package com.example.hospital_manager.controller;

import com.example.hospital_manager.dto.*;
import com.example.hospital_manager.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/records")
public class MedicalRecordController {
    private final MedicalRecordService service;
    public MedicalRecordController(MedicalRecordService service) { this.service=service; }
    @PostMapping @PreAuthorize("hasRole('DOCTOR')") public MedicalRecordResponse create(@Valid @RequestBody MedicalRecordRequest req, Authentication auth) { return service.create(req,auth.getName()); }
    @GetMapping("/patient/{id}") @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')") public List<MedicalRecordResponse> patient(@PathVariable Long id) { return service.byPatient(id); }
}
