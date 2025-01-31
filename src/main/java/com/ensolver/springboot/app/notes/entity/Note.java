package com.ensolver.springboot.app.notes.entity;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="notes")
public class Note {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String content;
    private String category;
    private boolean archived;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Crea la columna para la clave foránea en la tabla de notas
    @JsonIgnore
    private User user;
    
 // Constructor sin argumentos (requerido por JPA)
    public Note() {
    }

    // Constructor con argumentos (opcional, útil para testing)
    public Note(String title, String content, String category, boolean archived, User user) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.archived = archived;
        this.user = user;
    }
    // Getters y setters
    
    public Long getId() {
        return id;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    

}
