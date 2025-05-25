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

import com.emedina.hexagonal.ref.app.application.command.CreateArticleCommand;
import com.emedina.hexagonal.ref.app.application.ports.out.AuthorOutputPort;
import com.emedina.hexagonal.ref.app.domain.entities.Article;
import com.emedina.hexagonal.ref.app.domain.entities.ArticleId;
import com.emedina.hexagonal.ref.app.domain.entities.Author;
import com.emedina.hexagonal.ref.app.domain.entities.AuthorId;
import com.emedina.hexagonal.ref.app.domain.entities.Content;
import com.emedina.hexagonal.ref.app.domain.entities.PersonName;
import com.emedina.hexagonal.ref.app.domain.entities.Title;
import com.emedina.hexagonal.ref.app.domain.repositories.ArticleRepository;
import com.emedina.hexagonal.ref.app.shared.dto.AuthorDTO;
import com.emedina.hexagonal.ref.app.shared.error.Error;

import io.vavr.control.Either;

/**
 * Unit tests for CreateArticleHandler.
 *
 * @author Enrique Medina Montenegro
 */
@ExtendWith(MockitoExtension.class)
class CreateArticleHandlerTest {

    @Mock
    private AuthorOutputPort authorOutputPort;

    @Mock
    private ArticleRepository articleRepository;

    private CreateArticleHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CreateArticleHandler(authorOutputPort, articleRepository);
    }

    @Test
    void shouldCreateArticle_whenValidCommandProvided() {
        // given
        CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
            "article-123", "author-456", "Test Title", "Test content").get();

        AuthorDTO authorDTO = createValidAuthorDTO();
        Article expectedArticle = createValidArticle();

        when(authorOutputPort.lookupAuthor("author-456")).thenReturn(Either.right(authorDTO));
        when(articleRepository.save(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-456");
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void shouldReturnError_whenAuthorLookupFails() {
        // given
        CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
            "article-123", "author-456", "Test Title", "Test content").get();

        Error expectedError = new Error.MultipleErrors(java.util.List.of());

        when(authorOutputPort.lookupAuthor("author-456")).thenReturn(Either.left(expectedError));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(expectedError);
        verify(authorOutputPort).lookupAuthor("author-456");
        verify(articleRepository, org.mockito.Mockito.never()).save(any());
    }

    @Test
    void shouldReturnError_whenRepositorySaveFails() {
        // given
        CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
            "article-123", "author-456", "Test Title", "Test content").get();

        AuthorDTO authorDTO = createValidAuthorDTO();
        Error expectedError = new Error.MultipleErrors(java.util.List.of());

        when(authorOutputPort.lookupAuthor("author-456")).thenReturn(Either.right(authorDTO));
        when(articleRepository.save(any(Article.class))).thenReturn(Either.left(expectedError));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo(expectedError);
        verify(authorOutputPort).lookupAuthor("author-456");
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void shouldHandleComplexArticle_whenComplexCommandProvided() {
        // given
        CreateArticleCommand complexCommand = CreateArticleCommand.validateThenCreate(
            "article-complex-123",
            "author-complex-456",
            "Advanced Hexagonal Architecture: A Deep Dive into Ports and Adapters",
            "# Introduction\n\nThis comprehensive article explores...\n\n## Key Concepts\n\n- Domain isolation\n- Dependency inversion\n- Testability")
            .get();

        AuthorDTO authorDTO = new AuthorDTO("author-complex-456", "Jane Smith");

        when(authorOutputPort.lookupAuthor("author-complex-456")).thenReturn(Either.right(authorDTO));
        when(articleRepository.save(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(complexCommand);

        // then
        assertThat(result.isRight()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-complex-456");
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void shouldVerifyInteractionOrder_whenHandlingCommand() {
        // given
        CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
            "article-order", "author-order", "Order Test", "Testing interaction order").get();

        AuthorDTO authorDTO = createValidAuthorDTO();

        when(authorOutputPort.lookupAuthor("author-order")).thenReturn(Either.right(authorDTO));
        when(articleRepository.save(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();

        // Verify the order of interactions
        var inOrder = org.mockito.Mockito.inOrder(authorOutputPort, articleRepository);
        inOrder.verify(authorOutputPort).lookupAuthor("author-order");
        inOrder.verify(articleRepository).save(any(Article.class));
    }

    @Test
    void shouldNotCallRepository_whenAuthorLookupFails() {
        // given
        CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
            "article-fail", "author-fail", "Fail Test", "Testing author lookup failure").get();

        Error authorError = new Error.MultipleErrors(java.util.List.of());

        when(authorOutputPort.lookupAuthor("author-fail")).thenReturn(Either.left(authorError));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isLeft()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-fail");
        verify(articleRepository, org.mockito.Mockito.never()).save(any());
    }

    @Test
    void shouldHandleAuthorWithSpecialCharacters_whenValidAuthorProvided() {
        // given
        CreateArticleCommand command = CreateArticleCommand.validateThenCreate(
            "article-special", "author-special", "Special Test", "Testing special characters").get();

        AuthorDTO specialAuthor = new AuthorDTO("author-special", "José María García-López");

        when(authorOutputPort.lookupAuthor("author-special")).thenReturn(Either.right(specialAuthor));
        when(articleRepository.save(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(command);

        // then
        assertThat(result.isRight()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-special");
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void shouldHandleUnicodeContent_whenValidUnicodeCommandProvided() {
        // given
        CreateArticleCommand unicodeCommand = CreateArticleCommand.validateThenCreate(
            "article-unicode", "author-unicode", "Unicode Test: 软件架构", "Content with unicode: 你好世界 🌍").get();

        AuthorDTO authorDTO = createValidAuthorDTO();

        when(authorOutputPort.lookupAuthor("author-unicode")).thenReturn(Either.right(authorDTO));
        when(articleRepository.save(any(Article.class))).thenReturn(Either.right(null));

        // when
        Either<Error, Void> result = handler.handle(unicodeCommand);

        // then
        assertThat(result.isRight()).isTrue();
        verify(authorOutputPort).lookupAuthor("author-unicode");
        verify(articleRepository).save(any(Article.class));
    }

    private Article createValidArticle() {
        ArticleId id = ArticleId.validateThenCreate("article-123").get();
        Title title = Title.validateThenCreate("Test Title").get();
        Content content = Content.validateThenCreate("Test content").get();
        Author author = createValidAuthor();
        return Article.validateThenCreate(id, title, content, author).get();
    }

    private Author createValidAuthor() {
        AuthorId authorId = AuthorId.validateThenCreate("author-123").get();
        PersonName authorName = PersonName.validateThenCreate("John Doe").get();
        return Author.validateThenCreate(authorId, authorName).get();
    }

    private AuthorDTO createValidAuthorDTO() {
        return new AuthorDTO("author-123", "John Doe");
    }
}
