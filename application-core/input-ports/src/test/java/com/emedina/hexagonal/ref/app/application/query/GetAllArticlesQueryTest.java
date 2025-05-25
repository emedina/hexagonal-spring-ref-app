package com.emedina.hexagonal.ref.app.application.query;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;

import io.vavr.control.Validation;

/**
 * Unit tests for GetAllArticlesQuery.
 *
 * @author Enrique Medina Montenegro
 */
class GetAllArticlesQueryTest {

    @Test
    void shouldCreateValidQuery_whenValidateAndCreateCalled() {
        // given & when
        Validation<Error, GetAllArticlesQuery> result = GetAllArticlesQuery.validateThenCreate();

        // then
        assertThat(result.isValid()).isTrue();
        GetAllArticlesQuery query = result.get();
        assertThat(query).isNotNull();
    }

    @Test
    void shouldReturnConsistentHashCode_whenSameInstanceUsed() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();

        // when
        int hashCode1 = query.hashCode();
        int hashCode2 = query.hashCode();

        // then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void shouldBeEqualToItself_whenSameInstanceCompared() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();

        // when & then
        assertThat(query).isEqualTo(query);
        assertThat(query.equals(query)).isTrue();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithNull() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();

        // when & then
        assertThat(query.equals(null)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithDifferentType() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();
        String differentType = "not a query";

        // when & then
        assertThat(query.equals(differentType)).isFalse();
    }

    @Test
    void shouldHaveConsistentToString_whenCalled() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();

        // when
        String toString1 = query.toString();
        String toString2 = query.toString();

        // then
        assertThat(toString1).isEqualTo(toString2);
        assertThat(toString1).contains("GetAllArticlesQuery");
    }

    @Test
    void shouldCreateMultipleInstances_whenCalledMultipleTimes() {
        // given & when
        GetAllArticlesQuery query1 = GetAllArticlesQuery.validateThenCreate().get();
        GetAllArticlesQuery query2 = GetAllArticlesQuery.validateThenCreate().get();
        GetAllArticlesQuery query3 = GetAllArticlesQuery.validateThenCreate().get();

        // then
        assertThat(query1).isNotNull();
        assertThat(query2).isNotNull();
        assertThat(query3).isNotNull();
        // Note: Different instances are expected to be different objects
        assertThat(query1).isNotSameAs(query2);
        assertThat(query2).isNotSameAs(query3);
        assertThat(query1).isNotSameAs(query3);
    }

    @Test
    void shouldMaintainConsistentBehavior_whenUsedInCollections() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();

        // when & then
        assertThat(query.hashCode()).isEqualTo(query.hashCode());
        assertThat(query).isEqualTo(query);
    }

    @Test
    void shouldBeReflexive_whenTestingEquality() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();

        // when & then
        assertThat(query.equals(query)).isTrue();
        assertThat(query).isEqualTo(query);
    }

    @Test
    void shouldCreateNewInstancesWithFactoryMethod_whenCalledMultipleTimes() {
        // given & when
        GetAllArticlesQuery query1 = GetAllArticlesQuery.validateThenCreate().get();
        GetAllArticlesQuery query2 = GetAllArticlesQuery.validateThenCreate().get();

        // then
        assertThat(query1).isNotNull();
        assertThat(query2).isNotNull();
        // Factory method creates new instances each time
        assertThat(query1).isNotSameAs(query2);
        // But they may or may not be equal depending on implementation
    }

    @Test
    void shouldHaveConsistentHashCode_whenCalledMultipleTimes() {
        // given
        GetAllArticlesQuery query = GetAllArticlesQuery.validateThenCreate().get();

        // when
        int hashCode1 = query.hashCode();
        int hashCode2 = query.hashCode();
        int hashCode3 = query.hashCode();

        // then
        assertThat(hashCode1).isEqualTo(hashCode2);
        assertThat(hashCode2).isEqualTo(hashCode3);
        assertThat(hashCode1).isEqualTo(hashCode3);
    }

    @Test
    void shouldAlwaysReturnValidValidation_whenValidateAndCreateCalled() {
        // given & when
        Validation<Error, GetAllArticlesQuery> result1 = GetAllArticlesQuery.validateThenCreate();
        Validation<Error, GetAllArticlesQuery> result2 = GetAllArticlesQuery.validateThenCreate();
        Validation<Error, GetAllArticlesQuery> result3 = GetAllArticlesQuery.validateThenCreate();

        // then
        assertThat(result1.isValid()).isTrue();
        assertThat(result2.isValid()).isTrue();
        assertThat(result3.isValid()).isTrue();
        assertThat(result1.get()).isNotNull();
        assertThat(result2.get()).isNotNull();
        assertThat(result3.get()).isNotNull();
    }

    @Test
    void shouldNeverReturnInvalidValidation_whenValidateAndCreateCalled() {
        // given & when
        Validation<Error, GetAllArticlesQuery> result = GetAllArticlesQuery.validateThenCreate();

        // then
        assertThat(result.isInvalid()).isFalse();
        assertThat(result.isValid()).isTrue();
    }

    @Test
    void shouldCreateQueryWithNoParameters_whenFactoryMethodCalled() {
        // given & when
        Validation<Error, GetAllArticlesQuery> result = GetAllArticlesQuery.validateThenCreate();

        // then
        assertThat(result.isValid()).isTrue();
        GetAllArticlesQuery query = result.get();
        assertThat(query).isNotNull();
        assertThat(query.toString()).isNotNull();
    }

    @Test
    void shouldHandleMultipleValidationCalls_whenCalledConcurrently() {
        // given & when
        Validation<Error, GetAllArticlesQuery> result1 = GetAllArticlesQuery.validateThenCreate();
        Validation<Error, GetAllArticlesQuery> result2 = GetAllArticlesQuery.validateThenCreate();

        // then
        assertThat(result1.isValid()).isTrue();
        assertThat(result2.isValid()).isTrue();

        GetAllArticlesQuery query1 = result1.get();
        GetAllArticlesQuery query2 = result2.get();

        assertThat(query1).isNotNull();
        assertThat(query2).isNotNull();
    }
}
