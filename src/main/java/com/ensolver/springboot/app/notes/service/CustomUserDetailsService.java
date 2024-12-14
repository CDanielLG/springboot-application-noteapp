package com.ensolver.springboot.app.notes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		  // Buscar el usuario por email (o nombre de usuario) en la base de datos
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + username));

        // Retornar el objeto UserDetails, utilizando el correo y la contraseña del usuario
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword()) // Asegúrate de que la contraseña esté cifrada
                .roles("USER") // Aquí podrías agregar roles de ser necesario
                .build();
	}
	
}
