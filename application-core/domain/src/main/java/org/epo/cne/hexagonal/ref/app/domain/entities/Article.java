package org.epo.cne.hexagonal.ref.app.domain.entities;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;

import static org.epo.cne.hexagonal.ref.app.shared.validation.Validations.validateMandatory;

/**
 * Represents an article written by an author in our Domain Model.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Article {

    private final ArticleId id;
    private final Title title;
    private final Content content;
    private final Author author;

    public static Validation<Error, Article> validateThenCreate(final ArticleId id, final Title title,
                                                                final Content content, final Author author) {
        return Validation.combine(validateMandatory(id), validateMandatory(title), validateMandatory(content), validateMandatory(author))
                .ap((vid, vt, vc, va) -> new Article((ArticleId) vid, (Title) vt, (Content) vc, (Author) va))
                .mapError(e -> new Error.ValidationErrors(e.toJavaList()));
    }


    public Boolean validateEligibilityForPublication() {
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
