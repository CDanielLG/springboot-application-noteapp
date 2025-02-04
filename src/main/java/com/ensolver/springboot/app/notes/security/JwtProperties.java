package com.ensolver.springboot.app.notes.security;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;



@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    
    private String secret = "TUVTVUVSQ0xBVkVTQ1JFVEFIRU5CQVNlNjQ=";  // Asigna el valor directamente
    private Long expiration = 3600000L;  // 1 hora en milisegundos


    // Getters y Setters
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}