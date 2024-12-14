package com.ensolver.springboot.app.notes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ensolver.springboot.app.notes.DTO.RegisterRequest;
import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.repositories.UserRepository;

@Service
public class RegistrationService {
	   @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    // Método para registrar un usuario
	    public void registerUser(RegisterRequest registerRequest) {
	        if (userRepository.existsByEmail(registerRequest.getEmail())) {
	            throw new IllegalArgumentException("El correo ya está en uso");
	        }
	        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
	            throw new IllegalArgumentException("Las contraseñas no coinciden");
	        }

	        User user = new User();
	        user.setEmail(registerRequest.getEmail());
	        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
	        userRepository.save(user);
	    }

	    // Método para manejar login
	    public String loginUser(String email, String rawPassword) {
	        User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
	        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
	            return "Inicio de sesión exitoso";
	        } else {
	            throw new IllegalArgumentException("Credenciales inválidas");
	        }
	    }
}
