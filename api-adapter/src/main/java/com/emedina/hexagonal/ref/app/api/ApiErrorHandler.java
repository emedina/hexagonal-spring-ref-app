package com.emedina.hexagonal.ref.app.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.hexagonal.ref.app.shared.validation.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * API error handler that maps error values to HTTP status codes and problem details
 * using a list of {@link ProblemDetail}.
 *
 * @author Enrique Medina Montenegro
 */
@Slf4j
@Component
final class ApiErrorHandler {

    List<ProblemDetail> mapErrorToProblemDetail(final Error error, final HttpServletRequest request) {
        return switch (error) {
            case Error.ValidationErrors validationErrors ->
                    List.of(this.createProblemDetailFromValidationErrors(validationErrors));
            case Error.MultipleErrors multipleErrors ->
                    this.createProblemDetailFromMultipleErrors(multipleErrors, request);
            case Error.BusinessError.InvalidId invalidId ->
                    List.of(this.createProblemDetailFromBusinessError(invalidId));
            case Error.BusinessError.UnknownArticle unknownArticle ->
                    List.of(this.createProblemDetailFromBusinessError(unknownArticle));
            case Error.TechnicalError.SomethingWentWrong somethingWentWrong ->
                    List.of(this.createProblemDetailFromTechnicalError(somethingWentWrong, request));
        };
    }

    private ProblemDetail createProblemDetailFromTechnicalError(final Error.TechnicalError technicalError,
                                                                final HttpServletRequest request) {
        log.atTrace().setMessage("Technical error received").log();
        return switch (technicalError) {
            case Error.TechnicalError.SomethingWentWrong somethingWentWrongError -> {
                final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                problemDetail.setInstance(URI.create(request.getRequestURI()));
                problemDetail.setDetail("Internal server error [%s]".formatted(somethingWentWrongError.message()));
                yield problemDetail;
            }
        };
    }

    private ProblemDetail createProblemDetailFromBusinessError(final Error.BusinessError businessError) {
        log.atTrace().setMessage("Business error received").log();
        return switch (businessError) {
            case Error.BusinessError.InvalidId invalidId -> {
                final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
                problemDetail.setDetail("Article id [%s] not valid".formatted(invalidId.id()));
                yield problemDetail;
            }
            case Error.BusinessError.UnknownArticle unknownArticle -> {
                final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND.value());
                problemDetail.setDetail("Article with id [%s] not found".formatted(unknownArticle.id()));
                yield problemDetail;
            }
        };
    }

    private ProblemDetail createProblemDetailFromValidationErrors(final Error.ValidationErrors validationErrors) {
        log.atTrace().setMessage("Validation errors received, continue to map list of [{}]")
                .addArgument(validationErrors.errors()::size).log();
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST.value());
        problemDetail.setDetail(validationErrors.errors().stream()
                .map(this::createDetailFromValidationError).collect(Collectors.joining(", ")));

        return problemDetail;
    }

    private String createDetailFromValidationError(final ValidationError validationError) {
        return switch (validationError) {
            case ValidationError.Invalid invalid -> "Invalid value provided [%s]".formatted(invalid.value());
            case ValidationError.CannotBeNull cannotBeNull -> "Cannot be null [%s]".formatted(cannotBeNull.obj());
            case ValidationError.MustHaveContent mustHaveContent ->
                    "Must have content [%s]".formatted(mustHaveContent.name());
            case ValidationError.CannotBeEmpty cannotBeEmpty -> "Cannot be empty [%s]".formatted(cannotBeEmpty.list());
        };
    }

    private List<ProblemDetail> createProblemDetailFromMultipleErrors(final Error.MultipleErrors multipleErrors,
                                                                      final HttpServletRequest request) {
        log.atTrace().setMessage("Multiple errors received").log();
        return multipleErrors.errors().stream()
                .map(e -> this.mapErrorToProblemDetail(e, request))
                .flatMap(List::stream)
                .toList();
    }

}
