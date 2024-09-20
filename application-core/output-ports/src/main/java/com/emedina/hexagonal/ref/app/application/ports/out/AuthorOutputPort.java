package com.emedina.hexagonal.ref.app.application.ports.out;

import io.vavr.control.Either;
import com.emedina.hexagonal.ref.app.shared.dto.AuthorDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.application.annotation.OutputPort;

/**
 * This port abstracts away the usage of external services/APIs to retrieve the author
 * of a given article.
 *
 * @author Enrique Medina Montenegro
 */
@OutputPort
public interface AuthorOutputPort {

    Either<Error, AuthorDTO> lookupAuthor(final String id);

}
