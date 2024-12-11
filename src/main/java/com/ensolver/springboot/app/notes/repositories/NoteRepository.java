package com.ensolver.springboot.app.notes.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensolver.springboot.app.notes.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long>{

	
    List<Note> findByArchived(boolean archived);

}
