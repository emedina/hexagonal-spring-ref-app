package com.emedina.hexagonal.ref.app;

import com.emedina.hexagonal.application.handler.HandlerChecker;
import com.emedina.hexagonal.config.HexagonalArchitectureConfig;
import com.emedina.hexagonal.config.HexagonalArchitectureProperties;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Architecture test for the handlers.
 */
@AnalyzeClasses(packages = { "com.emedina.hexagonal.ref.app.application" })
@SpringBootTest(classes = { HexagonalArchitectureConfig.class, HandlerArchitectureTest.TestConfig.class })
class HandlerArchitectureTest {

    @Configuration
    static class TestConfig {

        @Bean
        public HandlerChecker handlerChecker(final HexagonalArchitectureProperties properties) {
            return new HandlerChecker(properties);
        }

    }

    @Test
    void loadProperties() {
        // Forces the loading of the properties in the Spring context...
    }

    @ArchTest
    public static final ArchTests handlerRules = ArchTests.in(HandlerChecker.class);

}
