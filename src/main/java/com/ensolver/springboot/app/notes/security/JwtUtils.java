package com.ensolver.springboot.app.notes.security;


import org.conscrypt.ct.DigitallySigned.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import java.util.Date;


@Component
public class JwtUtils {
	 private final String SECRET_KEY = "mySecretKey"; // Llave secreta para firmar el token
	    private final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

	    // Generar JWT
	    public String generateToken(String username) {
	        return Jwts.builder()
	                .setSubject(username)
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET_KEY)
	                .compact();
	    }

	    // Validar JWT
	    public boolean validateToken(String token) {
	        try {
	            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
	            return true;
	        } catch (Exception e) {
	            System.out.println("Token inválido: " + e.getMessage());
	            return false;
	        }
	    }

	    // Obtener el username (email) del token
	    public String getUsernameFromToken(String token) {
	        return Jwts.parser()
	                .setSigningKey(SECRET_KEY)
	                .parseClaimsJws(token)
	                .getBody()
	                .getSubject();
	    }
}
