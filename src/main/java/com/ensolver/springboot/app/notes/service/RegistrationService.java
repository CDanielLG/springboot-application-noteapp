package com.ensolver.springboot.app.notes.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ensolver.springboot.app.notes.DTO.RegisterRequest;
import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.repositories.UserRepository;
import com.ensolver.springboot.app.notes.security.JwtUtils;

@Service
public class RegistrationService {
	   @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    
	    @Autowired
	    private JwtUtils jwtUtils;

	    // Método para registrar un usuario
		public String registerUserAndGenerateToken(RegisterRequest registerRequest) {
			// Verificar si el correo ya está en uso
			if (userRepository.existsByEmail(registerRequest.getEmail())) {
				throw new IllegalArgumentException("El correo ya está en uso");
			}
		
			// Verificar si las contraseñas coinciden
			if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
				throw new IllegalArgumentException("Las contraseñas no coinciden");
			}
		
			// Registrar al usuario
			User user = new User();
			user.setEmail(registerRequest.getEmail());
			user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		
			// Asignar los roles como una lista
			user.setRoles(Collections.singletonList("ROLE_USER"));  // Usamos Collections.singletonList para crear una lista de un solo rol
		
			userRepository.save(user);
		
			// Generar token (asegurándonos de que el token se genera correctamente con los roles)
			String token = jwtUtils.generateToken(user); 
			return token;
		}

	    // Método para manejar login
	    public String loginUser(String email, String rawPassword) {
	    	   User user = userRepository.findByEmail(email)
	                   .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

	           // Validar contraseña
	           if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
	               throw new IllegalArgumentException("Credenciales inválidas");
	           }

	           // Generar token JWT
	           return jwtUtils.generateToken(user);
	    }
}
