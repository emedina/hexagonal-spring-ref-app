package org.epo.cne.hexagonal.ref.app.application.ports.out;

import io.vavr.control.Either;
import org.epo.cne.hexagonal.ref.app.shared.dto.AuthorDTO;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.application.annotation.OutputPort;

/**
 * This port abstracts away the usage of external services/APIs to retrieve the author
 * of a given article.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@OutputPort
public interface AuthorOutputPort {

    Either<Error, AuthorDTO> lookupAuthor(final String id);

}
