package com.emedina.hexagonal.ref.app;

import com.emedina.hexagonal.application.ports.out.OutputPortChecker;
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
 * Architecture test for the output ports.
 */
@AnalyzeClasses(packages = { "com.emedina.hexagonal.ref.app.application.ports.out" })
@SpringBootTest(classes = { HexagonalArchitectureConfig.class, OutputPortsArchitectureTest.TestConfig.class })
class OutputPortsArchitectureTest {

    @Configuration
    static class TestConfig {

        @Bean
        public OutputPortChecker outputPortChecker(final HexagonalArchitectureProperties properties) {
            return new OutputPortChecker(properties);
        }

    }

    @Test
    void loadProperties() {
        // Forces the loading of the properties in the Spring context...
    }

    @ArchTest
    public static final ArchTests outputPortsRules = ArchTests.in(OutputPortChecker.class);

}
