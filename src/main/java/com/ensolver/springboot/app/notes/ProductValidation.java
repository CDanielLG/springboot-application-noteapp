package com.ensolver.springboot.app.notes;



import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.ensolver.springboot.app.notes.entity.Note;

@Component
public class ProductValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Note.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Note product = (Note) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", null, "es requerido!");
        // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotBlank.product.description");
        if (product.getContent() == null || product.getContent().isBlank()) {
            errors.rejectValue("description", null, "es requerido, por favor");
        }

        if (product.getCategory() == null) {
            errors.rejectValue("price", null, "no puede ser nulo, ok!");
        } 

    }
    
}
