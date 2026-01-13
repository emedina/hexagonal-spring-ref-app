package com.emedina.hexagonal.ref.app;

import com.emedina.hexagonal.config.HexagonalArchitectureConfig;
import com.emedina.hexagonal.config.HexagonalArchitectureProperties;
import com.emedina.hexagonal.sharedkernel.SharedKernelChecker;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Architecture test for the shared kernel.
 */
@AnalyzeClasses(packages = "com.emedina.hexagonal.ref.app.shared")
@SpringBootTest(classes = { HexagonalArchitectureConfig.class, SharedKernelArchitectureTest.TestConfig.class })
final class SharedKernelArchitectureTest {

    @Configuration
    static class TestConfig {

        @Bean
        public SharedKernelChecker sharedKernelChecker(final HexagonalArchitectureProperties properties) {
            return new SharedKernelChecker(properties);
        }

    }

    @Test
    void loadProperties() {
        // Forces the loading of the properties in the Spring context...
    }

    @ArchTest
    public static final ArchTests sharedKernelRules = ArchTests.in(SharedKernelChecker.class);

}
