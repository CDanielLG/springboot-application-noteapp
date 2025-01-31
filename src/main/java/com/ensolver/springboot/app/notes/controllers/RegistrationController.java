package com.ensolver.springboot.app.notes.controllers;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ensolver.springboot.app.notes.DTO.RegisterRequest;
import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.exceptions.RegistrationException;
import com.ensolver.springboot.app.notes.security.SpringSecurityConfig;
import com.ensolver.springboot.app.notes.service.RegistrationService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "https://noteensolvers.web.app") // Permitir CORS desde el origen específico
@RestController
@RequestMapping("/public")
public class RegistrationController {

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        // Validar errores de validación
        if (result.hasFieldErrors()) {
            return validation(result);
        }
    
        // Buscar el usuario por email
        Optional<User> optionalUser = registrationService.findByEmail(loginRequest.getEmail());
    
        // Verificar si el usuario existe y si la contraseña coincide
        if (!optionalUser.isPresent() || !passwordEncoder.matches(loginRequest.getPassword(), optionalUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Credenciales incorrectas")); // Devuelve un JSON
        }
    
        // Si las credenciales son correctas, generar una respuesta exitosa
        User user = optionalUser.get();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Inicio de sesión exitoso.");
        response.put("user", user); // Evita devolver contraseñas u otra información sensible.
    
        return ResponseEntity.ok(response);
    }
    
    // Endpoint para registrar usuarios
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(registerRequest.getPassword());
            user.setAdmin(false); // Por defecto, no es admin

            User registeredUser = registrationService.save(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RegistrationException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

}

