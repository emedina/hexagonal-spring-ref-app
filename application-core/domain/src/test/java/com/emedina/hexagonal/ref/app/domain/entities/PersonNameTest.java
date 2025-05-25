package com.emedina.hexagonal.ref.app.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;

import io.vavr.control.Validation;

/**
 * Unit tests for PersonName.
 *
 * @author Enrique Medina Montenegro
 */
class PersonNameTest {

    @Test
    void shouldCreateValidPersonName_whenValidStringProvided() {
        // given
        String validName = "John Doe";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(validName);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(validName);
    }

    @Test
    void shouldReturnValidationError_whenNullNameProvided() {
        // given
        String nullName = null;

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(nullName);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldReturnValidationError_whenEmptyNameProvided() {
        // given
        String emptyName = "";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(emptyName);

        // then
        assertThat(result.isInvalid()).isTrue();
        assertThat(result.getError()).isInstanceOf(Error.ValidationErrors.class);
        Error.ValidationErrors validationErrors = (Error.ValidationErrors) result.getError();
        assertThat(validationErrors.errors()).hasSize(1);
        assertThat(validationErrors.errors().get(0)).isInstanceOf(ValidationError.Invalid.class);
    }

    @Test
    void shouldCreatePersonNameWithSingleName_whenValidSingleNameProvided() {
        // given
        String singleName = "Madonna";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(singleName);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(singleName);
    }

    @Test
    void shouldCreatePersonNameWithMultipleNames_whenValidMultipleNamesProvided() {
        // given
        String multipleName = "Jean-Baptiste Grenouille";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(multipleName);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(multipleName);
    }

    @Test
    void shouldCreatePersonNameWithTitles_whenValidNameWithTitlesProvided() {
        // given
        String nameWithTitle = "Dr. Martin Luther King Jr.";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(nameWithTitle);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(nameWithTitle);
    }

    @Test
    void shouldCreatePersonNameWithApostrophes_whenValidNameWithApostrophesProvided() {
        // given
        String nameWithApostrophe = "O'Connor";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(nameWithApostrophe);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(nameWithApostrophe);
    }

    @Test
    void shouldCreatePersonNameWithHyphens_whenValidNameWithHyphensProvided() {
        // given
        String nameWithHyphen = "Mary-Jane Watson";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(nameWithHyphen);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(nameWithHyphen);
    }

    @Test
    void shouldCreatePersonNameWithUnicodeCharacters_whenValidUnicodeNameProvided() {
        // given
        String unicodeName = "José María García-López";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(unicodeName);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(unicodeName);
    }

    @Test
    void shouldCreatePersonNameWithAsianCharacters_whenValidAsianNameProvided() {
        // given
        String asianName = "山田太郎";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(asianName);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(asianName);
    }

    @Test
    void shouldCreatePersonNameWithCyrillicCharacters_whenValidCyrillicNameProvided() {
        // given
        String cyrillicName = "Владимир Путин";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(cyrillicName);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(cyrillicName);
    }

    @Test
    void shouldReturnSameHashCode_whenTwoPersonNamesHaveSameValue() {
        // given
        String name = "William Shakespeare";
        PersonName personName1 = PersonName.validateThenCreate(name).get();
        PersonName personName2 = PersonName.validateThenCreate(name).get();

        // when
        int hashCode1 = personName1.hashCode();
        int hashCode2 = personName2.hashCode();

        // then
        assertThat(hashCode1).isEqualTo(hashCode2);
    }

    @Test
    void shouldBeEqual_whenTwoPersonNamesHaveSameValue() {
        // given
        String name = "William Shakespeare";
        PersonName personName1 = PersonName.validateThenCreate(name).get();
        PersonName personName2 = PersonName.validateThenCreate(name).get();

        // when & then
        assertThat(personName1).isEqualTo(personName2);
        assertThat(personName1.equals(personName2)).isTrue();
    }

    @Test
    void shouldNotBeEqual_whenTwoPersonNamesHaveDifferentValues() {
        // given
        PersonName personName1 = PersonName.validateThenCreate("John Doe").get();
        PersonName personName2 = PersonName.validateThenCreate("Jane Smith").get();

        // when & then
        assertThat(personName1).isNotEqualTo(personName2);
        assertThat(personName1.equals(personName2)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithNull() {
        // given
        PersonName personName = PersonName.validateThenCreate("John Doe").get();

        // when & then
        assertThat(personName.equals(null)).isFalse();
    }

    @Test
    void shouldNotBeEqual_whenComparedWithDifferentType() {
        // given
        PersonName personName = PersonName.validateThenCreate("John Doe").get();
        String stringValue = "John Doe";

        // when & then
        assertThat(personName.equals(stringValue)).isFalse();
    }

    @Test
    void shouldReturnValue_whenValueMethodCalled() {
        // given
        String expectedValue = "Albert Einstein";
        PersonName personName = PersonName.validateThenCreate(expectedValue).get();

        // when
        String actualValue = personName.value();

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void shouldCreatePersonNameWithLongName_whenValidLongNameProvided() {
        // given
        String longName = "Pablo Diego José Francisco de Paula Juan Nepomuceno María de los Remedios Cipriano de la Santísima Trinidad Ruiz y Picasso";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(longName);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(longName);
    }

    @Test
    void shouldCreatePersonNameWithNumbers_whenValidNameWithNumbersProvided() {
        // given
        String nameWithNumbers = "Elizabeth II";

        // when
        Validation<Error, PersonName> result = PersonName.validateThenCreate(nameWithNumbers);

        // then
        assertThat(result.isValid()).isTrue();
        assertThat(result.get().value()).isEqualTo(nameWithNumbers);
    }
}
