package com.sushma.jobtracker;

import com.sushma.jobtracker.service.JwtService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

@Configuration
public class TempJwtTest {

    @Bean
    public CommandLineRunner testJwt(JwtService jwt) {
        return args -> {
            UserDetails fakeUser = new User(
                    "test@sushma.com",
                    "irrelevant",
                    Collections.emptyList()
            );

            String token = jwt.generateToken(fakeUser);
            String emailFromToken = jwt.extractUsername(token);
            boolean valid = jwt.isTokenValid(token, fakeUser);

            System.out.println("================================");
            System.out.println("Generated JWT:");
            System.out.println(token);
            System.out.println();
            System.out.println("Email extracted: " + emailFromToken);
            System.out.println("Valid?           " + valid);
            System.out.println("================================");
        };
    }
}