package com.emedina.hexagonal.ref.app;

import com.emedina.hexagonal.application.query.QueryChecker;
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
 * Architecture test for the queries.
 */
@AnalyzeClasses(packages = { "com.emedina.hexagonal.ref.app.application.query" })
@SpringBootTest(classes = { HexagonalArchitectureConfig.class, QueryArchitectureTest.TestConfig.class })
class QueryArchitectureTest {

    @Configuration
    static class TestConfig {

        @Bean
        public QueryChecker queryChecker(final HexagonalArchitectureProperties properties) {
            return new QueryChecker(properties);
        }

    }

    @Test
    void loadProperties() {
        // Forces the loading of the properties in the Spring context...
    }

    @ArchTest
    public static final ArchTests queryRules = ArchTests.in(QueryChecker.class);

}
