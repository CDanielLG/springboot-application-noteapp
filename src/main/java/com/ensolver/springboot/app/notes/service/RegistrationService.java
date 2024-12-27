package com.ensolver.springboot.app.notes.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.ensolver.springboot.app.notes.entity.Role;
import com.ensolver.springboot.app.notes.entity.User;
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
	    
	    @Transactional(readOnly = true)
	    public List<User> findAll() {
	        return (List<User>) userRepository.findAll();
	    }
	    

	    @Transactional
	    public User save(User user) {

	        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
	        List<Role> roles = new ArrayList<>();

	        optionalRoleUser.ifPresent(roles::add);

	        if (user.isAdmin()) {
	            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
	            optionalRoleAdmin.ifPresent(roles::add);
	        }

	        user.setRoles(roles);
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        return userRepository.save(user);
	    }

	    public boolean existsByUsername(String username) {
	        return userRepository.existsByEmail(username);
	    }
}
