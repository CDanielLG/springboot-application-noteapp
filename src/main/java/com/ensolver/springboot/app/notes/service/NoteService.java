package com.ensolver.springboot.app.notes.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ensolver.springboot.app.notes.entity.Note;
import com.ensolver.springboot.app.notes.entity.User;
import com.ensolver.springboot.app.notes.repositories.NoteRepository;
import com.ensolver.springboot.app.notes.repositories.UserRepository;


@Service
public class NoteService {
	

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public List<Note> getNotesByArchived(boolean archived) {
        return noteRepository.findByArchived(archived);
    }

   
    @Transactional
    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }
    @Transactional
    public Note createNoteForUser(Note note, String userEmail) {
        if (note.getTitle() == null || note.getTitle().isEmpty()) {
            throw new IllegalArgumentException("El título de la nota no puede estar vacío");
        }
        if (note.getContent() == null || note.getContent().isEmpty()) {
            throw new IllegalArgumentException("El contenido de la nota no puede estar vacío");
        }
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    
        note.setUser(user); // Asigna el usuario a la nota
        return noteRepository.save(note);
    }

    public void deleteNote(Long id) {
        noteRepository.deleteById(id);
    }

    public void toggleArchive(Long id) {
        Note note = noteRepository.findById(id).orElseThrow();
        note.setArchived(!note.isArchived());
        noteRepository.save(note);
    }
    public void updateNote(Note note) {
        noteRepository.save(note); // Guardar la nota actualizada en la base de datos
    }
    
    
    public List<String> getAllCategories() {
        List<Note> notes = noteRepository.findAll();  // Obtener todas las notas
        Set<String> categories = new HashSet<>();
        for (Note note : notes) {
            if (note.getCategory() != null) {
                categories.add(note.getCategory());
            }
        }
        return new ArrayList<>(categories);
    }

    public List<Note> getNotesByUserEmail(String email) {
        return noteRepository.findByUser_Email(email); // Supone que existe esta consulta en el repositorio
    }

	public boolean existsBySku(String value) {
		
		return false;
	}
	
}
