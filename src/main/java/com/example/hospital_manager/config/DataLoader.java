package com.example.hospital_manager.config;

import com.example.hospital_manager.model.*;
import com.example.hospital_manager.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {
    @Bean CommandLineRunner seedData(UserRepository users, PatientRepository patients, PasswordEncoder encoder) {
        return args -> {
            if (!users.existsByUsername("admin")) users.save(AppUser.builder().username("admin")
              .password(encoder.encode("Admin@123")).fullName("System Administrator").role(Role.ADMIN).build());
            if (!users.existsByUsername("doctor1")) users.save(AppUser.builder().username("doctor1")
              .password(encoder.encode("Doctor@123")).fullName("Dr. Sample").role(Role.DOCTOR).build());
            if (!users.existsByUsername("patient1")) {
                users.save(AppUser.builder().username("patient1").password(encoder.encode("Patient@123"))
                  .fullName("Sample Patient").role(Role.PATIENT).build());
            }
            if (patients.count()==0) patients.save(Patient.builder().name("Sample Patient")
              .username("patient1").email("patient@example.com").phone("0712345678").age(25).build());
        };
    }
}
