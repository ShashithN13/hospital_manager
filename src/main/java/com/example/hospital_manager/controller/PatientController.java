package com.example.hospital_manager.controller;

import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/patients")
public class PatientController {
    private final PatientService service;
    public PatientController(PatientService service) { this.service=service; }
    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')") public List<Patient> all() { return service.all(); }
    @GetMapping("/search") @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')") public List<Patient> search(@RequestParam String name) { return service.search(name); }
    @GetMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')") public Patient get(@PathVariable Long id) { return service.get(id); }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')") public Patient create(@Valid @RequestBody Patient p) { return service.create(p); }
    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')") public Patient update(@PathVariable Long id,@Valid @RequestBody Patient p) { return service.update(id,p); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable Long id) { service.delete(id); }
}
