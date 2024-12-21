package com.ensolver.springboot.app.notes.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensolver.springboot.app.notes.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long>{

	
    List<Note> findByArchived(boolean archived);
    // Encuentra notas por el email del usuario
    List<Note> findByUserEmail(String email);

    // Encuentra notas por el ID del usuario
    List<Note> findByUserId(Long userId);

    List<Note> findByUser_Email(String email);
}
