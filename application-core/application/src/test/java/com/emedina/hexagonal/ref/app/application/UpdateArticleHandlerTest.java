package com.emedina.hexagonal.ref.app.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.emedina.hexagonal.ref.app.application.command.UpdateArticleCommand;
import com.emedina.hexagonal.ref.app.application.ports.out.AuthorOutputPort;
import com.emedina.hexagonal.ref.app.domain.entities.Article;
import com.emedina.hexagonal.ref.app.domain.repositories.ArticleRepository;
import com.emedina.hexagonal.ref.app.shared.dto.AuthorDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;

import io.vavr.control.Either;

/**
 * Unit tests for UpdateArticleHandler.
 *
 * @author Enrique Medina Montenegro
 */
@ExtendWith(MockitoExtension.class)
class UpdateArticleHandlerTest {

    @Mock
    private AuthorOutputPort authorOutputPort;

    @Mock
    private ArticleRepository articleRepository;

    private UpdateArticleHandler handler;

    @BeforeEach
    void setUp() {
        handler = new UpdateArticleHandler(authorOutputPort, articleRepository);
    }

    @Test
    void shouldUpdateArticle_whenValidCommandProvided() {
        // given
        UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
            "article-123", "author-456", "Updated Title", "Updated content").get();

        AuthorDTO authorDTO = createValidAuthorDTO();

        when(authorOutputPort.lookupAuthor("author-456")).thenReturn(Either.right(authorDTO));
        when(articleRepository.update(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-456");
        verify(articleRepository).update(any(Article.class));
    }

    @Test
    void shouldReturnError_whenAuthorLookupFails() {
        // given
        UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
            "article-123", "author-456", "Updated Title", "Updated content").get();

        Error expectedError = new Error.MultipleErrors(java.util.List.of());

        when(authorOutputPort.lookupAuthor("author-456")).thenReturn(Either.left(expectedError));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(expectedError);
        verify(authorOutputPort).lookupAuthor("author-456");
        verify(articleRepository, org.mockito.Mockito.never()).update(any());
    }

    @Test
    void shouldReturnError_whenRepositoryUpdateFails() {
        // given
        UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
            "article-123", "author-456", "Updated Title", "Updated content").get();

        AuthorDTO authorDTO = createValidAuthorDTO();
        Error expectedError = new Error.MultipleErrors(java.util.List.of());

        when(authorOutputPort.lookupAuthor("author-456")).thenReturn(Either.right(authorDTO));
        when(articleRepository.update(any(Article.class))).thenReturn(Either.left(expectedError));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(expectedError);
        verify(authorOutputPort).lookupAuthor("author-456");
        verify(articleRepository).update(any(Article.class));
    }

    @Test
    void shouldHandleComplexUpdate_whenComplexCommandProvided() {
        // given
        UpdateArticleCommand complexCommand = UpdateArticleCommand.validateThenCreate(
            "article-complex-123",
            "author-complex-456",
            "Updated: Advanced Hexagonal Architecture Patterns",
            "# Updated Introduction\n\nThis updated article now includes:\n\n" +
                "1. **Enhanced Hexagonal Architecture** patterns\n" +
                "2. **Updated Clean Architecture** examples\n" +
                "3. **Revised Onion Architecture** concepts\n\n" +
                "## Updated Code Examples\n\n" +
                "```java\n" +
                "public class UpdatedDomainService {\n" +
                "    // Updated implementation\n" +
                "}\n" +
                "```")
            .get();

        AuthorDTO authorDTO = new AuthorDTO("author-complex-456", "Jane Smith");

        when(authorOutputPort.lookupAuthor("author-complex-456")).thenReturn(Either.right(authorDTO));
        when(articleRepository.update(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(complexCommand);

        // then
        assertThat(result.isRight()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-complex-456");
        verify(articleRepository).update(any(Article.class));
    }

    @Test
    void shouldVerifyInteractionOrder_whenHandlingCommand() {
        // given
        UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
            "article-order", "author-order", "Order Test", "Testing interaction order").get();

        AuthorDTO authorDTO = createValidAuthorDTO();

        when(authorOutputPort.lookupAuthor("author-order")).thenReturn(Either.right(authorDTO));
        when(articleRepository.update(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();

        // Verify the order of interactions
        var inOrder = org.mockito.Mockito.inOrder(authorOutputPort, articleRepository);
        inOrder.verify(authorOutputPort).lookupAuthor("author-order");
        inOrder.verify(articleRepository).update(any(Article.class));
    }

    @Test
    void shouldNotCallRepository_whenAuthorLookupFails() {
        // given
        UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
            "article-fail", "author-fail", "Fail Test", "Testing author lookup failure").get();

        Error authorError = new Error.MultipleErrors(java.util.List.of());

        when(authorOutputPort.lookupAuthor("author-fail")).thenReturn(Either.left(authorError));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isLeft()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-fail");
        verify(articleRepository, org.mockito.Mockito.never()).update(any());
    }

    @Test
    void shouldHandleAuthorWithSpecialCharacters_whenValidAuthorProvided() {
        // given
        UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(
            "article-special", "author-special", "Special Update Test", "Testing special characters in update").get();

        AuthorDTO specialAuthor = new AuthorDTO("author-special", "José María García-López");

        when(authorOutputPort.lookupAuthor("author-special")).thenReturn(Either.right(specialAuthor));
        when(articleRepository.update(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-special");
        verify(articleRepository).update(any(Article.class));
    }

    @Test
    void shouldHandleUnicodeContent_whenValidUnicodeCommandProvided() {
        // given
        UpdateArticleCommand unicodeCommand = UpdateArticleCommand.validateThenCreate(
            "article-unicode", "author-unicode", "Unicode Update Test: 软件架构", "Updated content with unicode: 你好世界 🌍")
            .get();

        AuthorDTO authorDTO = createValidAuthorDTO();

        when(authorOutputPort.lookupAuthor("author-unicode")).thenReturn(Either.right(authorDTO));
        when(articleRepository.update(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(unicodeCommand);

        // then
        assertThat(result.isRight()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-unicode");
        verify(articleRepository).update(any(Article.class));
    }

    @Test
    void shouldHandleMinorUpdate_whenSmallChangesProvided() {
        // given
        UpdateArticleCommand minorCommand = UpdateArticleCommand.validateThenCreate(
            "article-minor", "author-minor", "Introduction to Hexagonal Architecture (v2)",
            "This article explains the principles of hexagonal architecture. Updated with new examples.").get();

        AuthorDTO authorDTO = createValidAuthorDTO();

        when(authorOutputPort.lookupAuthor("author-minor")).thenReturn(Either.right(authorDTO));
        when(articleRepository.update(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(minorCommand);

        // then
        assertThat(result.isRight()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-minor");
        verify(articleRepository).update(any(Article.class));
    }

    @Test
    void shouldHandleMajorUpdate_whenExtensiveChangesProvided() {
        // given
        UpdateArticleCommand majorCommand = UpdateArticleCommand.validateThenCreate(
            "article-major", "author-major",
            "Complete Guide to Hexagonal Architecture: From Theory to Practice",
            "# Complete Rewrite\n\n" +
                "This is a completely updated version of the original article.\n\n" +
                "## New Sections\n\n" +
                "- Advanced patterns\n" +
                "- Real-world examples\n" +
                "- Performance considerations\n" +
                "- Testing strategies\n\n" +
                "## Updated Code Examples\n\n" +
                "All code examples have been updated to use the latest frameworks and best practices.")
            .get();

        AuthorDTO authorDTO = createValidAuthorDTO();

        when(authorOutputPort.lookupAuthor("author-major")).thenReturn(Either.right(authorDTO));
        when(articleRepository.update(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(majorCommand);

        // then
        assertThat(result.isRight()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-major");
        verify(articleRepository).update(any(Article.class));
    }

    private AuthorDTO createValidAuthorDTO() {
        return new AuthorDTO("author-123", "John Doe");
    }
}
