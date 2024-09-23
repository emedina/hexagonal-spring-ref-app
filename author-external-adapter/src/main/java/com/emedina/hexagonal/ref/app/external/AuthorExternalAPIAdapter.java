package com.emedina.hexagonal.ref.app.external;

import com.emedina.hexagonal.ref.app.application.ports.out.AuthorOutputPort;
import com.emedina.hexagonal.ref.app.shared.dto.AuthorDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.application.annotation.Adapter;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

/**
 * Implementation of {@link AuthorOutputPort} that uses an external API to fetch information about authors.
 *
 * @author Enrique Medina Montenegro
 */
@Slf4j
@Adapter
@RequiredArgsConstructor
class AuthorExternalAPIAdapter implements AuthorOutputPort {

    private final RestClient restClient;

    /**
     * Lookup the author with the given id.
     *
     * @param id the id of the author to lookup
     * @return either an error or the author with the given id
     */
    @Override
    public Either<Error, AuthorDTO> lookupAuthor(String id) {
        // Pretend we are calling an external API...
        // this.restClient.get().uri("https://api.example.com/authors/{id}", id).retrieve().toEntity(AuthorDTO.class);
        return Either.right(new AuthorDTO(id, "William Shakespeare"));
    }

}
