package com.ensolver.springboot.app.notes.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensolver.springboot.app.notes.DTO.RegisterRequest;
import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.repositories.UserRepository;
import com.ensolver.springboot.app.notes.service.RegistrationService;

@RestController
@RequestMapping("/public")
public class RegistrationController {
	

    @Autowired
    private RegistrationService registrationService;

    // Endpoint para registrar usuarios
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            registrationService.registerUser(registerRequest);
            return ResponseEntity.ok("Usuario registrado con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para login (puedes decidir manejarlo con Spring Security)
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody RegisterRequest loginRequest) {
        String response = registrationService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }

}

