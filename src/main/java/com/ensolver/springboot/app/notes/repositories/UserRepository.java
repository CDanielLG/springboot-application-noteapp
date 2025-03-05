package com.ensolver.springboot.app.notes.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensolver.springboot.app.notes.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	 Optional<User> findByUsername(String username);
	 boolean existsByUsername(String username);


}
