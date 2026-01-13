package com.emedina.hexagonal.ref.app;

import com.emedina.hexagonal.application.domain.DomainChecker;
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
 * Architecture test for the domain.
 */
@AnalyzeClasses(packages = { "com.emedina.hexagonal.ref.app.domain" })
@SpringBootTest(classes = { HexagonalArchitectureConfig.class, DomainArchitectureTest.TestConfig.class })
class DomainArchitectureTest {

    @Configuration
    static class TestConfig {

        @Bean
        public DomainChecker domainChecker(final HexagonalArchitectureProperties properties) {
            return new DomainChecker(properties);
        }

    }

    @Test
    void loadProperties() {
        // Forces the loading of the properties in the Spring context...
    }

    @ArchTest
    public static final ArchTests domainRules = ArchTests.in(DomainChecker.class);

}
