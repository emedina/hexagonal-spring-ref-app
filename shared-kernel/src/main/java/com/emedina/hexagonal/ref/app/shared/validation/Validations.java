package com.emedina.hexagonal.ref.app.shared.validation;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import io.vavr.control.Try;
import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class to provide different types of validations.
 *
 * @author Enrique Medina Montenegro
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validations {

    /**
     * Validates if the provided text is valid.
     *
     * @param text the text to be validated
     * @return the validation result
     */
    public static Validation<ValidationError, String> validateText(final String text) {
        return validateMandatoryString(text) ?
            Validation.valid(text) :
            Validation.invalid(new ValidationError.Invalid(text));
    }

    /**
     * Validates if the provided number is greater than zero.
     *
     * @param number the number to be validated
     * @return the validation result
     */
    public static Validation<ValidationError, Long> validateGreaterThanZero(final Long number) {
        return Objects.nonNull(number) && number > 0L ?
            Validation.valid(number) :
            Validation.invalid(new ValidationError.CannotBeNull(number));
    }

    /**
     * Validates if the provided stream is not null.
     *
     * @param stream the stream to be validated
     * @return the validation result
     */
    public static Validation<ValidationError, InputStream> validateContent(final InputStream stream,
        final String name) {
        return Objects.nonNull(stream) && Try.of(() -> stream.available() > 0).getOrElse(false) ?
            Validation.valid(stream) :
            Validation.invalid(new ValidationError.MustHaveContent(name));
    }

    /**
     * Validates if the provided list is not empty
     *
     * @param list the list to be validated
     * @return the validation result
     */
    public static Validation<ValidationError, List<?>> validateNotEmpty(final List<?> list) {
        return Objects.nonNull(list) && !list.isEmpty() ?
            Validation.valid(list) :
            Validation.invalid(new ValidationError.CannotBeEmpty(list));
    }

    /**
     * Validate if a value exists.
     *
     * @param value value that should be tested
     * @return true if not null, false otherwise
     */
    public static Validation<ValidationError, Object> validateMandatory(Object value) {
        return Objects.nonNull(value) ?
            Validation.valid(value) :
            Validation.invalid(new ValidationError.CannotBeNull(value));
    }

    /**
     * Validate if a string exists, and it's not empty.
     *
     * @param value value that should be tested
     * @return true if not null and not empty, false otherwise
     */
    private static boolean validateMandatoryString(String value) {
        return Objects.nonNull(value) && !value.isEmpty();
    }

}
