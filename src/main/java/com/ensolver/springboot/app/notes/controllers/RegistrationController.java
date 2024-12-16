package com.ensolver.springboot.app.notes.controllers;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensolver.springboot.app.notes.DTO.RegisterRequest;
import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.repositories.UserRepository;
import com.ensolver.springboot.app.notes.service.RegistrationService;

@CrossOrigin(origins = "https://noteensolvers.web.app") // Permitir CORS desde el origen específico
@RestController
@RequestMapping("/public")
public class RegistrationController {
	

    @Autowired
    private RegistrationService registrationService;

    // Endpoint para registrar usuarios
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            String token = registrationService.registerUserAndGenerateToken(registerRequest); // Registrar y generar token
            Map<String, String> response = Map.of("message", "Usuario registrado con éxito", "token", token);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = Map.of("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Endpoint para login (puedes decidir manejarlo con Spring Security)
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody RegisterRequest loginRequest) {
        try {
            String token = registrationService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(Map.of("token", token)); // Devuelve un JSON con el token
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage())); // Maneja errores
        }
    }

}

