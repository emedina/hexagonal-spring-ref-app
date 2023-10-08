package org.epo.cne.hexagonal.ref.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main bootstrap class for the Spring Boot application.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@SpringBootApplication(proxyBeanMethods = false, scanBasePackages = "org.epo")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
