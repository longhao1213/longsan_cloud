package com.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.security.mapper")
public class Security6Application {

    public static void main(String[] args) {
        SpringApplication.run(Security6Application.class, args);
    }
}
