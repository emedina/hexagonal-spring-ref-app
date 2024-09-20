package com.emedina.hexagonal.ref.app.domain.entities;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import com.emedina.hexagonal.ref.app.shared.error.Error;

import static com.emedina.hexagonal.ref.app.shared.validation.Validations.validateMandatory;

/**
 * Represents an article written by an author in our Domain Model.
 *
 * @author Enrique Medina Montenegro
 */
@Getter
@Accessors(fluent = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {

    @EqualsAndHashCode.Include
    private final ArticleId id;

    private final Title title;
    private final Content content;
    private final Author author;

    /**
     * Validates the mandatory fields of an article and creates a new instance of it.
     *
     * @param id      the identifier of the article
     * @param title   the title of the article
     * @param content the content of the article
     * @param author  the author of the article
     * @return a new instance of an article if all the mandatory fields are valid
     */
    public static Validation<Error, Article> validateThenCreate(final ArticleId id, final Title title,
                                                                final Content content, final Author author) {
        return Validation.combine(validateMandatory(id), validateMandatory(title), validateMandatory(content), validateMandatory(author))
                .ap((vid, vt, vc, va) -> new Article((ArticleId) vid, (Title) vt, (Content) vc, (Author) va))
                .mapError(e -> new Error.ValidationErrors(e.toJavaList()));
    }

    public Boolean enforceEligibilityForPublication() {
        this.verifyForPlagiarism();
        this.validateTitleLength();
        this.validateContentLength();
        this.checkPunctuation();
        this.checkGrammar();
        this.checkStyle();
        //TODO: these methods are just placeholders with empty implementation

        return true;
    }

    private void checkGrammar() {
    }

    private void checkStyle() {
    }

    private void checkPunctuation() {
    }

    private void verifyForPlagiarism() {
    }

    private void validateContentLength() {
    }

    private void validateTitleLength() {
    }

}
