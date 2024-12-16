package com.ensolver.springboot.app.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ensolver.springboot.app.notes.security.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class SpringbootApplicationNoteappApplication {
	public static void main(String[] args) {
	
		
		SpringApplication.run(SpringbootApplicationNoteappApplication.class, args);
	}

}
