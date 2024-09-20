package com.emedina.hexagonal.ref.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main bootstrap class for the Spring Boot application.
 *
 * @author Enrique Medina Montenegro
 */
@SpringBootApplication(proxyBeanMethods = false, scanBasePackages = "com.emedina")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
