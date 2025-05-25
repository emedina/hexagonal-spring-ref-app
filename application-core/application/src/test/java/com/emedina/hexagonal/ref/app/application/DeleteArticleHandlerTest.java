package com.emedina.hexagonal.ref.app.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.emedina.hexagonal.ref.app.application.command.DeleteArticleCommand;
import com.emedina.hexagonal.ref.app.domain.entities.ArticleId;
import com.emedina.hexagonal.ref.app.domain.repositories.ArticleRepository;
import com.emedina.hexagonal.ref.app.shared.error.Error;

import io.vavr.control.Either;

/**
 * Unit tests for DeleteArticleHandler.
 *
 * @author Enrique Medina Montenegro
 */
@ExtendWith(MockitoExtension.class)
class DeleteArticleHandlerTest {

    @Mock
    private ArticleRepository articleRepository;

    private DeleteArticleHandler handler;

    @BeforeEach
    void setUp() {
        handler = new DeleteArticleHandler(articleRepository);
    }

    @Test
    void shouldDeleteArticle_whenValidCommandProvided() {
        // given
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate("article-123").get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate("article-123").get();

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldReturnError_whenDeleteOperationFails() {
        // given
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate("article-fail").get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate("article-fail").get();
        Error deleteError = new Error.MultipleErrors(java.util.List.of());

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.left(deleteError));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(deleteError);
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldHandleUuidId_whenValidUuidProvided() {
        // given
        String uuidId = "550e8400-e29b-41d4-a716-446655440000";
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate(uuidId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(uuidId).get();

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldHandleSpecialCharactersInId_whenValidIdProvided() {
        // given
        String specialId = "article-123_test@domain.com";
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate(specialId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(specialId).get();

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldHandleLongId_whenValidLongIdProvided() {
        // given
        String longId = "article-" + "a".repeat(100);
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate(longId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(longId).get();

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldHandleUnicodeId_whenValidUnicodeIdProvided() {
        // given
        String unicodeId = "article-测试-тест-🌍";
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate(unicodeId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(unicodeId).get();

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldHandleNumericId_whenValidNumericIdProvided() {
        // given
        String numericId = "12345";
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate(numericId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(numericId).get();

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldHandleSlugId_whenValidSlugProvided() {
        // given
        String slugId = "introduction-to-hexagonal-architecture";
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate(slugId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(slugId).get();

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldHandleVersionedId_whenValidVersionedIdProvided() {
        // given
        String versionedId = "article-123-v2.1.0";
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate(versionedId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(versionedId).get();

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldHandleUrlLikeId_whenValidUrlLikeIdProvided() {
        // given
        String urlLikeId = "https://example.com/articles/123";
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate(urlLikeId).get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate(urlLikeId).get();

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldReturnError_whenRepositoryReturnsSpecificError() {
        // given
        DeleteArticleCommand command = DeleteArticleCommand.validateThenCreate("article-error").get();
        ArticleId expectedArticleId = ArticleId.validateThenCreate("article-error").get();
        Error specificError = new Error.MultipleErrors(java.util.List.of());

        when(articleRepository.delete(expectedArticleId)).thenReturn(Either.left(specificError));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(specificError);
        verify(articleRepository).delete(expectedArticleId);
    }

    @Test
    void shouldHandleMultipleDeleteOperations_whenCalledSequentially() {
        // given
        DeleteArticleCommand command1 = DeleteArticleCommand.validateThenCreate("article-1").get();
        DeleteArticleCommand command2 = DeleteArticleCommand.validateThenCreate("article-2").get();
        ArticleId articleId1 = ArticleId.validateThenCreate("article-1").get();
        ArticleId articleId2 = ArticleId.validateThenCreate("article-2").get();

        when(articleRepository.delete(articleId1)).thenReturn(Either.right(null));
        when(articleRepository.delete(articleId2)).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result1 = handler.handle(command1);
        Either<Error, Void> result2 = handler.handle(command2);

        // then
        assertThat(result1.isRight()).isTrue();
        assertThat(result2.isRight()).isTrue();
        verify(articleRepository).delete(articleId1);
        verify(articleRepository).delete(articleId2);
    }
}
