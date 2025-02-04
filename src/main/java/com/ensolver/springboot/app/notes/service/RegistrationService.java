package com.ensolver.springboot.app.notes.service;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.ensolver.springboot.app.notes.entity.Role;
import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.exceptions.RegistrationException;
import com.ensolver.springboot.app.notes.repositories.RoleRepository;
import com.ensolver.springboot.app.notes.repositories.UserRepository;


@Service
public class RegistrationService {
	
	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    
	    @Autowired
	    private RoleRepository roleRepository;
	      

		@Transactional
		public User save(User user) {
		 // Validar campos obligatorios
		 if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RegistrationException("El correo no puede estar vacío");
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RegistrationException("La contraseña no puede estar vacía");
        }

        // Verificar si el correo ya existe
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RegistrationException("El correo ya está registrado");
        }

        // Asignar roles
        List<Role> roles = new ArrayList<>();

        // Rol USER por defecto (obligatorio)
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RegistrationException("Rol USER no encontrado"));
        roles.add(userRole);

        // Rol ADMIN (si está configurado)
        if (user.isAdmin()) { // Asegúrate de que isAdmin() esté correctamente implementado
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RegistrationException("Rol ADMIN no encontrado"));
            roles.add(adminRole);
        }

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptar contraseña

        return userRepository.save(user);
		}
	
		@Transactional(readOnly = true)
		public boolean existsByEmail(String email) {
			return userRepository.existsByEmail(email);
		}
	
		@Transactional(readOnly = true)
		public Optional<User> findByEmail(String email) {
			return userRepository.findByEmail(email);
		}
	}