package com.emedina.hexagonal.ref.app.application.command;

import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.command.Command;

import java.util.List;

import static com.emedina.hexagonal.ref.app.shared.validation.Validations.validateText;

/**
 * Command to encapsulate a request to delete an article.
 *
 * @author Enrique Medina Montenegro
 */
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteArticleCommand implements Command {

    private final String id;

    public static Validation<Error, DeleteArticleCommand> validateThenCreate(final String id) {
        return validateText(id)
                .map(DeleteArticleCommand::new)
                .mapError(e -> new Error.ValidationErrors(List.of(e)));
    }

}
