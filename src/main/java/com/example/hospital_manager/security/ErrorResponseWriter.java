package com.example.hospital_manager.security;

import jakarta.servlet.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class ErrorResponseWriter {
    public void write(HttpServletRequest req, HttpServletResponse res, HttpStatus status, String message) throws IOException {
        res.setStatus(status.value());
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json");
        String body = "{\"timestamp\":" + quote(LocalDateTime.now().toString())
                + ",\"status\":" + status.value()
                + ",\"error\":" + quote(status.getReasonPhrase())
                + ",\"message\":" + quote(message)
                + ",\"path\":" + quote(req.getRequestURI()) + "}";
        res.getWriter().write(body);
    }

    private static String quote(String value) {
        if (value == null) return "null";

        StringBuilder escaped = new StringBuilder(value.length() + 2).append('"');
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            switch (character) {
                case '"': escaped.append("\\\""); break;
                case '\\': escaped.append("\\\\"); break;
                case '\b': escaped.append("\\b"); break;
                case '\f': escaped.append("\\f"); break;
                case '\n': escaped.append("\\n"); break;
                case '\r': escaped.append("\\r"); break;
                case '\t': escaped.append("\\t"); break;
                default:
                    if (character < 0x20) {
                        escaped.append("\\u00");
                        escaped.append("0123456789abcdef".charAt((character >> 4) & 0x0f));
                        escaped.append("0123456789abcdef".charAt(character & 0x0f));
                    } else {
                        escaped.append(character);
                    }
            }
        }
        return escaped.append('"').toString();
    }
}
