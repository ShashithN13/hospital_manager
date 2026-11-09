package com.example.hospital_manager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ErrorResponseWriter {
    private final ObjectMapper mapper;
    public ErrorResponseWriter(ObjectMapper mapper) { this.mapper = mapper; }
    public void write(HttpServletRequest req, HttpServletResponse res, HttpStatus status, String message) throws IOException {
        res.setStatus(status.value()); res.setContentType("application/json");
        mapper.writeValue(res.getOutputStream(), Map.of("timestamp", LocalDateTime.now().toString(),
          "status", status.value(), "error", status.getReasonPhrase(), "message", message, "path", req.getRequestURI()));
    }
}
