package com.ensolver.springboot.app.notes.security;



import org.springframework.stereotype.Component;
import com.ensolver.springboot.app.notes.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;


@Component
public class JwtUtils {

	    private final JwtProperties jwtProperties;

	   
	    public JwtUtils(JwtProperties jwtProperties) {
	        this.jwtProperties = jwtProperties;
	    }

	    // Generar un token JWT
	    public String generateToken(User user) {
	        return Jwts.builder()
	                .setSubject(user.getEmail())
	                .setIssuedAt(new Date())
	                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
	                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, jwtProperties.getSecret())
	                .compact();
	    }

	    // Validar y obtener datos del token
	    public Claims getClaimsFromToken(String token) {
	        return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
	    }

	    // Validar si el token ha expirado
	    public boolean isTokenExpired(String token) {
	        return getClaimsFromToken(token).getExpiration().before(new Date());
	    }
	    public boolean validateToken(String token) {
	        return !isTokenExpired(token); // Puedes agregar más validaciones aquí si es necesario.
	    }

		public String extractEmail(String token) {
			Claims claims = Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
			return claims.getSubject();
		}
	}

