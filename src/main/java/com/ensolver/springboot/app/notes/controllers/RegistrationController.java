package com.ensolver.springboot.app.notes.controllers;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;

import io.jsonwebtoken.security.Keys;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.ensolver.springboot.app.notes.DTO.RegisterRequest;

import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.exceptions.RegistrationException;

import com.ensolver.springboot.app.notes.service.RegistrationService;

import io.jsonwebtoken.Jwts;

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
    
        // Si las credenciales son correctas, generar un token
        User user = optionalUser.get();
        String token = generateToken(user); // Implementa este método si usas JWT
    
        // Devolver el token en la respuesta
        return ResponseEntity.ok(Map.of("token", token));
    }
    
    // Endpoint para registrar usuarios
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setAdmin(false); // Por defecto, no es admin

            User registeredUser = registrationService.save(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RegistrationException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
    public String generateToken(User user) {
          // Define una clave secreta segura
    String secretString = "tu_clave_secreta_muy_larga_y_segura"; // Usa una clave larga y segura
    SecretKey key = Keys.hmacShaKeyFor(secretString.getBytes());

    // Crear los claims (puedes personalizarlos según tus necesidades)
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", user.getEmail()); // Agregar el email del usuario
    claims.put("id", user.getId()); // Agregar el ID del usuario

    // Agregar los roles del usuario a los claims (si existen)
    if (user.getRoles() != null && !user.getRoles().isEmpty()) {
        String roles = user.getRoles().stream()
                .map(role -> role.getName()) // Suponiendo que Role tiene un método getName()
                .collect(Collectors.joining(",")); // Convertir la lista de roles a una cadena separada por comas
        claims.put("roles", roles); // Agregar los roles al token
    }

    return Jwts.builder()
            .claims(claims) // Agregar los claims
            .subject(user.getEmail()) // Subject del token
            .issuedAt(new Date()) // Fecha de emisión
            .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de expiración
            .signWith(key) // Firma con la clave secreta
            .compact(); // Genera el token
    }

}

