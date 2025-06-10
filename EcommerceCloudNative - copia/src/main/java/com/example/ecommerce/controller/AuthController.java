package com.example.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        // Validación simple - en producción esto debería usar Spring Security properly
        if ("Admin".equals(username) && "CloudNative_123".equals(password)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("user", username);
            // En un caso real, aquí generarías un JWT token
            response.put("token", "Basic " + java.util.Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes()));
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    @GetMapping("/verify")
    public ResponseEntity<?> verify() {
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("message", "Token válido");
        return ResponseEntity.ok(response);
    }
}