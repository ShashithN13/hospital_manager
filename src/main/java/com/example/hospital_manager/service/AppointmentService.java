package com.example.hospital_manager.service;

import com.example.hospital_manager.dto.*;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.*;
import com.example.hospital_manager.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointments; private final PatientRepository patients; private final UserRepository users;
    public AppointmentService(AppointmentRepository a, PatientRepository p, UserRepository u) { appointments=a; patients=p; users=u; }
    public AppointmentResponse book(AppointmentRequest req) {
        Patient patient=patients.findById(req.patientId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Patient not found"));
        AppUser doctor=users.findById(req.doctorId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Doctor not found"));
        if (doctor.getRole()!=Role.DOCTOR) throw new ApiException(HttpStatus.BAD_REQUEST,"Selected user is not a doctor");
        Appointment a=appointments.save(Appointment.builder().patient(patient).doctor(doctor).appointmentDateTime(req.appointmentDateTime()).status(AppointmentStatus.PENDING).build());
        return toResponse(a);
    }
    public List<AppointmentResponse> all() { return appointments.findAll().stream().map(this::toResponse).toList(); }
    public List<AppointmentResponse> byDoctor(String username) {
        AppUser doctor=users.findByUsername(username).orElseThrow();
        return appointments.findByDoctorId(doctor.getId()).stream().map(this::toResponse).toList();
    }
    public List<AppointmentResponse> byPatient(Long id) { return appointments.findByPatientId(id).stream().map(this::toResponse).toList(); }
    public AppointmentResponse updateStatus(Long id, StatusRequest req) {
        Appointment a=appointments.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"Appointment not found"));
        a.setStatus(req.status()); return toResponse(appointments.save(a));
    }
    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(a.getId(),a.getPatient().getId(),a.getPatient().getName(),a.getDoctor().getId(),
          a.getDoctor().getFullName(),a.getAppointmentDateTime(),a.getStatus().name(),a.getNotes());
    }
}
