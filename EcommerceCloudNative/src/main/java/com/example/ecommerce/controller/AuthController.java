package com.example.ecommerce.controller;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.LoginResponse;
import com.example.ecommerce.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Validación simple - en producción deberías usar UserDetailsService
        if ("Admin".equals(loginRequest.getUsername()) && 
            "CloudNative_123".equals(loginRequest.getPassword())) {
            
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            
            LoginResponse response = new LoginResponse(
                true,
                "Login exitoso",
                token,
                loginRequest.getUsername()
            );
            
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
    
    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                response.put("authenticated", true);
                response.put("message", "Token válido");
                response.put("username", jwtUtil.extractUsername(token));
                return ResponseEntity.ok(response);
            }
        }
        
        response.put("authenticated", false);
        response.put("message", "Token inválido o ausente");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}