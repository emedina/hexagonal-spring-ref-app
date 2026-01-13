package com.emedina.hexagonal.ref.app;

import com.emedina.hexagonal.application.ports.in.InputPortChecker;
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
 * Architecture test for the input ports.
 */
@AnalyzeClasses(packages = { "com.emedina.hexagonal.ref.app.application.ports.in" })
@SpringBootTest(classes = { HexagonalArchitectureConfig.class, InputPortsArchitectureTest.TestConfig.class })
class InputPortsArchitectureTest {

    @Configuration
    static class TestConfig {

        @Bean
        public InputPortChecker inputPortChecker(final HexagonalArchitectureProperties properties) {
            return new InputPortChecker(properties);
        }

    }

    @Test
    void loadProperties() {
        // Forces the loading of the properties in the Spring context...
    }

    @ArchTest
    public static final ArchTests inputPortsRules = ArchTests.in(InputPortChecker.class);

}
