package com.ensolver.springboot.app.notes.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.CrossOrigin;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensolver.springboot.app.notes.DTO.LoginRequest;
import com.ensolver.springboot.app.notes.DTO.RegisterRequest;

import com.ensolver.springboot.app.notes.service.RegistrationService;


import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*") // Permitir CORS desde el origen específico
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class RegistrationController {
    
    
    private final RegistrationService authService;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }
}

