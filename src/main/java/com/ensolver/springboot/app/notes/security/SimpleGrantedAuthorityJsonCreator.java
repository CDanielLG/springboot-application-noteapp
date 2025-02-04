package com.ensolver.springboot.app.notes.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jackson2.SimpleGrantedAuthorityMixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJsonCreator extends SimpleGrantedAuthorityMixin implements GrantedAuthority {

    @JsonCreator
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {
        super(role);
    }
}
