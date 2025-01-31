package com.ensolver.springboot.app.notes.exceptions;

public class RegistrationException extends RuntimeException {
    public RegistrationException(String message){
        super(message);
    }
    
}
