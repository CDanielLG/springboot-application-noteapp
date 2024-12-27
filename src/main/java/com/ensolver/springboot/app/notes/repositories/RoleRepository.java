package com.ensolver.springboot.app.notes.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ensolver.springboot.app.notes.entity.Role;



public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);
    
}
