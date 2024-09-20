package com.emedina.hexagonal.ref.app.api;

import io.vavr.control.Either;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.emedina.hexagonal.ref.app.application.command.CreateArticleCommand;
import com.emedina.hexagonal.ref.app.application.command.DeleteArticleCommand;
import com.emedina.hexagonal.ref.app.application.command.UpdateArticleCommand;
import com.emedina.hexagonal.ref.app.application.query.FindArticleQuery;
import com.emedina.hexagonal.ref.app.application.query.GetAllArticlesQuery;
import com.emedina.hexagonal.ref.app.shared.dto.ArticleDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;
import com.emedina.sharedkernel.command.core.CommandBus;
import com.emedina.sharedkernel.query.core.QueryBus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Implementation of the API interface using a REST controller.
 *
 * @author Enrique Medina Montenegro
 */
@Slf4j
@RestController
@RequiredArgsConstructor
final class ArticleController implements ArticleApi {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    private final ApiErrorHandler apiErrorHandler;

    /**
     * @see ArticleApi#get(HttpServletRequest)
     */
    @Override
    public ResponseEntity<?> get(final HttpServletRequest request) {
        log.atTrace().log(Thread.currentThread().getName());
        return GetAllArticlesQuery.validateThenCreate()
                .toEither()
                .<List<ArticleDTO>>flatMap(this.queryBus::query)
                .mapLeft(e -> this.apiErrorHandler.mapErrorToProblemDetail(e, request))
                .fold(lpd -> ApiResultUtils.createFailureResponse(lpd, URI.create(request.getRequestURI())),
                        a -> ApiResultUtils.createSuccessListResponse(HttpStatus.OK,
                                a.stream().<ApiResponse>map(ApiMapper.INSTANCE::toArticleResponse).toList()));
    }

    /**
     * @see ArticleApi#find(String, HttpServletRequest)
     */
    @Override
    public ResponseEntity<?> find(@PathVariable("articleId") final String articleId,
                                  final HttpServletRequest request) {
        return FindArticleQuery.validateThenCreate(articleId)
                .toEither()
                .<ArticleDTO>flatMap(this.queryBus::query)
                .mapLeft(e -> this.apiErrorHandler.mapErrorToProblemDetail(e, request))
                .fold(lpd -> ApiResultUtils.createFailureResponse(lpd, URI.create(request.getRequestURI())),
                        a -> ApiResultUtils.createSuccessResponse(HttpStatus.OK, ApiMapper.INSTANCE.toArticleResponse(a)));
    }

    /**
     * @see ArticleApi#create(ApiRequest.Article, HttpServletRequest)
     */
    @Override
    public ResponseEntity<?> create(@RequestBody final ApiRequest.Article articleRequest,
                                    final HttpServletRequest request) {
        return CreateArticleCommand.validateThenCreate(articleRequest.id(), articleRequest.authorId(),
                        articleRequest.title(), articleRequest.content())
                .toEither()
                .flatMap(cac -> (Either<Error, Void>) this.commandBus.execute(cac))
                .mapLeft(e -> this.apiErrorHandler.mapErrorToProblemDetail(e, request))
                .fold(lpd -> ApiResultUtils.createFailureResponse(lpd, URI.create(request.getRequestURI())),
                        a -> ApiResultUtils.createSuccessResponse(HttpStatus.CREATED, null));
    }

    /**
     * @see ArticleApi#update(ApiRequest.Article, HttpServletRequest)
     */
    @Override
    public ResponseEntity<?> update(@RequestBody final ApiRequest.Article articleRequest, final HttpServletRequest request) {
        return UpdateArticleCommand.validateThenCreate(articleRequest.id(), articleRequest.authorId(),
                        articleRequest.title(), articleRequest.content())
                .toEither()
                .flatMap(uac -> (Either<Error, Void>) this.commandBus.execute(uac))
                .mapLeft(e -> this.apiErrorHandler.mapErrorToProblemDetail(e, request))
                .fold(lpd -> ApiResultUtils.createFailureResponse(lpd, URI.create(request.getRequestURI())),
                        a -> ApiResultUtils.createSuccessResponse(HttpStatus.OK, null));
    }

    /**
     * @see ArticleApi#delete(String, HttpServletRequest)
     */
    @Override
    public ResponseEntity<?> delete(@PathVariable("articleId") final String articleId, final HttpServletRequest request) {
        return DeleteArticleCommand.validateThenCreate(articleId)
                .toEither()
                .flatMap(dac -> (Either<Error, Void>) this.commandBus.execute(dac))
                .mapLeft(e -> this.apiErrorHandler.mapErrorToProblemDetail(e, request))
                .fold(lpd -> ApiResultUtils.createFailureResponse(lpd, URI.create(request.getRequestURI())),
                        a -> ApiResultUtils.createSuccessResponse(HttpStatus.OK, null));
    }

}
