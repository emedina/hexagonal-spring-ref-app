package com.emedina.hexagonal.ref.app;

import com.emedina.hexagonal.adapters.AdapterChecker;
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
 * Architecture test for the adapters.
 */
@AnalyzeClasses(
    packages = {
        "com.emedina.hexagonal.ref.app.api..",
        "com.emedina.hexagonal.ref.app.repositories..",
        "com.emedina.hexagonal.ref.app.external.."
    }
)
@SpringBootTest(classes = { HexagonalArchitectureConfig.class, AdaptersArchitectureTest.TestConfig.class })
class AdaptersArchitectureTest {

    @Configuration
    static class TestConfig {

        @Bean
        public AdapterChecker adapterChecker(final HexagonalArchitectureProperties properties) {
            return new AdapterChecker(properties);
        }

    }

    @Test
    void loadProperties() {
        // Forces the loading of the properties in the Spring context...
    }

    @ArchTest
    public static final ArchTests adaptersRules = ArchTests.in(AdapterChecker.class);

}
