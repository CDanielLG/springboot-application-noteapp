package com.ensolver.springboot.app.notes.controllers;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ensolver.springboot.app.notes.DTO.RegisterRequest;
import com.ensolver.springboot.app.notes.entity.User;

import com.ensolver.springboot.app.notes.service.RegistrationService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "https://noteensolvers.web.app") // Permitir CORS desde el origen específico
@RestController
@RequestMapping("/public")
public class RegistrationController {


   
    
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RegistrationService registrationService;

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
    
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody RegisterRequest loginRequest, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
    
        User user = registrationService.findByEmail(loginRequest.getEmail());
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }
    
        // Generar un token o simplemente indicar éxito
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Inicio de sesión exitoso.");
        response.put("user", user); // Evita devolver contraseñas u otra información sensible.
    
        return ResponseEntity.ok(response);
    }
    
    // Endpoint para registrar usuarios
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
    
        // Verificar si el email ya está en uso
        if (registrationService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "El correo electrónico ya está registrado."));
        }
    
        // Codificar la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    
        // Configurar el rol predeterminado
        user.setAdmin(false);
    
        // Guardar el usuario
        User savedUser = registrationService.save(user);
    
        // Preparar la respuesta (sin incluir información sensible como la contraseña)
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario registrado exitosamente.");
        response.put("user", Map.of(
            "id", savedUser.getId(),
            "email", savedUser.getEmail(),
            "admin", savedUser.isAdmin()
        ));
    
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}

