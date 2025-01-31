package com.ensolver.springboot.app.notes.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ensolver.springboot.app.notes.service.*;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistsByUsernameValidation implements ConstraintValidator<ExistsByUsername, String> {

    @Autowired
    private RegistrationService service;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (service == null) {
            return true;
        }
        return  !service.existsByEmail(email);
    }
    
    
}
