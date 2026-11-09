package com.example.hospital_manager.service;

import com.example.hospitalmanager.dto.*;
import com.example.hospitalmanager.exception.ApiException;
import com.example.hospitalmanager.model.*;
import com.example.hospitalmanager.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicalRecordService {
    private final MedicalRecordRepository records; private final PatientRepository patients; private final UserRepository users;
    public MedicalRecordService(MedicalRecordRepository r, PatientRepository p, UserRepository u) { records=r; patients=p; users=u; }
    public MedicalRecordResponse create(MedicalRecordRequest req, String doctorUsername) {
        Patient p=patients.findById(req.patientId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Patient not found"));
        AppUser doctor=users.findByUsername(doctorUsername).orElseThrow();
        MedicalRecord r=records.save(MedicalRecord.builder().patient(p).doctor(doctor).diagnosis(req.diagnosis()).prescription(req.prescription()).build());
        return toResponse(r);
    }
    public List<MedicalRecordResponse> byPatient(Long id) { return records.findByPatientId(id).stream().map(this::toResponse).toList(); }
    private MedicalRecordResponse toResponse(MedicalRecord r) {
        return new MedicalRecordResponse(r.getId(),r.getPatient().getId(),r.getPatient().getName(),r.getDoctor().getFullName(),r.getDiagnosis(),r.getPrescription(),r.getCreatedAt());
    }
}

