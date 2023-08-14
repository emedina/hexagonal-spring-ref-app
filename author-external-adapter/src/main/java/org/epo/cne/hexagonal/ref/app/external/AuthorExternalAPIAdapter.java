package org.epo.cne.hexagonal.ref.app.external;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epo.cne.hexagonal.ref.app.application.ports.out.AuthorOutputPort;
import org.epo.cne.hexagonal.ref.app.shared.dto.AuthorDTO;
import org.epo.cne.hexagonal.ref.app.shared.error.Error;
import org.epo.cne.sharedkernel.application.annotation.Adapter;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Implementation of {@link AuthorOutputPort} that uses an external API to fetch information about authors.
 *
 * @author Enrique Medina Montenegro (em54029)
 */
@Slf4j
@Adapter
@RequiredArgsConstructor
class AuthorExternalAPIAdapter implements AuthorOutputPort {

    private final WebClient webClient;

    /**
     * Lookup the author with the given id.
     *
     * @param id the id of the author to lookup
     * @return either an error or the author with the given id
     */
    @Override
    public Either<Error, AuthorDTO> lookupAuthor(String id) {
        // Pretend we are calling an external API...
        // this.webClient.get().uri("https://api.example.com/authors/{id}", id).retrieve().bodyToMono(AuthorDTO.class).block();
        return Either.right(new AuthorDTO(id, "William Shakespeare"));
    }

}
