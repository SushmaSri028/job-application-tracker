package com.sushma.jobtracker;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class TempPasswordTest {

    @Bean
    public CommandLineRunner testPasswordHashing(PasswordEncoder encoder) {
        return args -> {
            String plain = "mySecret123";
            String hashed = encoder.encode(plain);

            System.out.println("================================");
            System.out.println("Plain:  " + plain);
            System.out.println("Hashed: " + hashed);
            System.out.println("Match:  " + encoder.matches(plain, hashed));
            System.out.println("Wrong:  " + encoder.matches("wrongpass", hashed));
            System.out.println("================================");
        };
    }
}