package com.ensolver.springboot.app.notes.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ensolver.springboot.app.notes.entity.Note;
import com.ensolver.springboot.app.notes.repositories.NoteRepository;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;



@Service
public class NoteService {
	

    @Autowired
    private NoteRepository noteRepository;

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public List<Note> getNotesByArchived(boolean archived) {
        return noteRepository.findByArchived(archived);
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }

    public Note createNote(Note note) {
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
	
}
