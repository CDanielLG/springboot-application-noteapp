package com.ensolver.springboot.app.notes.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ensolver.springboot.app.notes.entity.Note;
import com.ensolver.springboot.app.notes.service.NoteService;

@CrossOrigin(origins = "*") // Permitir CORS desde el origen específico
@RestController
@RequestMapping("/api/notes")
public class NotesController {
	
	    @Autowired
	    private NoteService noteService;


		@GetMapping("/api/notes/test")
public ResponseEntity<String> testAuth() {
    String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok("Usuario autenticado: " + userEmail);
}
		private String getAuthenticatedUserEmail() {
			return SecurityContextHolder.getContext().getAuthentication().getName();
		}
	    // Obtener todas las notas
	    @GetMapping
		@PreAuthorize("hasAnyRole('USER','ADMIN')") 
		public ResponseEntity<List<Note>> getAllNotes() {
			String userEmail = getAuthenticatedUserEmail(); // Obtén el email del usuario autenticado
			List<Note> notes = noteService.getNotesByUserEmail(userEmail); // Filtra las notas
			return new ResponseEntity<>(notes, HttpStatus.OK);
		}

	    // Obtener notas archivadas o activas
	    @GetMapping("/status/{archived}")
	    public ResponseEntity<List<Note>> getNotesByStatus(@PathVariable boolean archived) {
	        List<Note> notes = noteService.getNotesByArchived(archived);
	        return new ResponseEntity<>(notes, HttpStatus.OK);
	    }

	    // Crear una nueva nota
		@PostMapping
		@PreAuthorize("hasAnyRole('USER','ADMIN')") // Solo usuarios con rol USER o ADMIN pueden crear notas
		public ResponseEntity<Note> createNote(@RequestBody Note note) {
			String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
			Note createdNote = noteService.createNoteForUser(note, userEmail);
			return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
		}
	    //Edit a note
	    @PutMapping("/{id}")
	    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note note) {
	        Optional<Note> existingNote = noteService.getNoteById(id);

	        if (existingNote.isPresent()) {
	            Note updatedNote = existingNote.get();
	            updatedNote.setTitle(note.getTitle());
	            updatedNote.setContent(note.getContent());
	            updatedNote.setCategory(note.getCategory());
	            noteService.updateNote(updatedNote); // Actualiza la nota en el servicio

	            return new ResponseEntity<>(updatedNote, HttpStatus.OK); // Devuelve la nota actualizada
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Si no se encuentra la nota, devuelve 404
	        }
	    }

	    // Eliminar una nota
	    @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
	        noteService.deleteNote(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }

	    // Archivar o desarchivar una nota
	    @PatchMapping("/{id}/archive")
	    public ResponseEntity<Void> toggleArchive(@PathVariable Long id) {
	        noteService.toggleArchive(id);
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	    @GetMapping("/categories")
	    public ResponseEntity<List<String>> getAllCategories() {
	        List<String> categories = noteService.getAllCategories(); // Aquí debes implementar el método
	        return new ResponseEntity<>(categories, HttpStatus.OK);
	    }
}
