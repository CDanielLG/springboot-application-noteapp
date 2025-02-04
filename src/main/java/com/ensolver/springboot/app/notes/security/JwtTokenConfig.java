package com.ensolver.springboot.app.notes.security;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;

@Configuration
public class JwtTokenConfig{


    private final JwtProperties jwtProperties;


    public JwtTokenConfig(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Bean
    public SecretKey secretKey() {
        // Usamos Keys.hmacShaKeyFor() para asegurar que la clave sea de al menos 256 bits
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CONTENT_TYPE = "application/json";
}

