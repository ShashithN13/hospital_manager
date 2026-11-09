package com.example.hospital_manager.service;

import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patients; private final AppointmentRepository appointments; private final MedicalRecordRepository records;
    public PatientService(PatientRepository p, AppointmentRepository a, MedicalRecordRepository r) { patients=p; appointments=a; records=r; }
    public Patient create(Patient patient) { return patients.save(patient); }
    public List<Patient> all() { return patients.findAll(); }
    public List<Patient> search(String name) { return patients.findByNameContainingIgnoreCase(name); }
    public Patient get(Long id) { return patients.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Patient not found")); }
    public Patient update(Long id, Patient input) {
        Patient p=get(id); p.setName(input.getName()); p.setEmail(input.getEmail()); p.setPhone(input.getPhone()); p.setAge(input.getAge()); return patients.save(p);
    }
    public void delete(Long id) {
        get(id);
        if (appointments.existsByPatientId(id) || records.existsByPatientId(id)) throw new ApiException(HttpStatus.CONFLICT,"Cannot delete patient with appointments or medical records");
        patients.deleteById(id);
    }
}
