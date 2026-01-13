# 🔷 Hexagonal Spring Reference Application

![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Java Version](https://img.shields.io/badge/Java-25-blue)
![Test Coverage](https://img.shields.io/badge/coverage-90%25-brightgreen)

A comprehensive reference implementation of the Hexagonal Architecture (Ports and Adapters) pattern using the Spring ecosystem, demonstrating clean separation of concerns and domain-driven design principles.

## 📚 Further Learning

This reference application demonstrates patterns and principles thoroughly explored in:

**English Version**  
*Decoupling by Design: A Pragmatic Approach to Hexagonal Architecture*  

- [PDF](https://leanpub.com/decouplingbydesignapractitionersguidetohexagonalarchitecture)  
- [Kindle](https://a.co/d/4KwauyK)  
- [Paperback](https://a.co/d/cGQI8gX)  

**Versión en Español**  
*Desacoplamiento por Diseño: Una Guía Práctica para la Arquitectura Hexagonal*  

- [PDF](https://leanpub.com/desacoplamientopordiseounaguaprcticaparalaarquitecturahexagonal)  
- [Kindle](https://amzn.eu/d/ic50CoH)  
- [Tapa blanda](https://amzn.eu/d/1fHOpN6)  

The book provides complete coverage of:

- End-to-end hexagonal architecture implementation
- Domain-driven design integration
- Spring Boot configuration for hexagonal systems
- Functional error handling patterns
- Adapter implementation strategies
- Testing approaches for each architectural layer
- Real-world case studies and evolution patterns
- Migration from traditional layered architectures

## 🎯 Overview

This project serves as a practical example of implementing the Hexagonal Architecture with Spring Boot. It showcases how to structure a Spring application with clear boundaries between the domain core and external dependencies, enabling better testability, maintainability, and flexibility.

## ✨ Features

- **🔷 Pure Hexagonal Architecture**: Complete implementation of ports and adapters pattern
- **🧩 Modular Structure**: Clear separation between domain, application, and infrastructure layers
- **🔄 Functional Error Handling**: Using Vavr's Either type for exception-free error management
- **🧪 Comprehensive Testing**: High test coverage across all architectural layers
- **📝 Article Management**: Full CRUD operations for article entities
- **👤 Author Integration**: External author service integration via adapters
- **🔌 Pluggable Repositories**: Swappable data storage implementations
- **🛡️ Validation**: Robust input validation with clear error reporting
- **🚀 Spring Boot Integration**: Seamless integration with Spring ecosystem

## 📦 Installation

Add the project as a dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.emedina</groupId>
    <artifactId>hexagonal-spring-ref-app</artifactId>
    <version>0.0.1</version>
</dependency>
```

Or clone the repository to explore and run locally:

```bash
git clone https://github.com/emedina/hexagonal-spring-ref-app.git
cd hexagonal-spring-ref-app
mvn clean install
```

## 🚀 Quick Start

### 1️⃣ Run the Application

```bash
cd spring-boot-assembly
mvn spring-boot:run
```

### 2️⃣ API Usage Examples

#### Create an Article

```java
// Using the API adapter
HttpEntity<ApiRequest.Article> request = new HttpEntity<>(
    new ApiRequest.Article("article-123", "author-456", "My Article Title", "Article content goes here")
);
ResponseEntity<Void> response = restTemplate.postForEntity("/api/articles", request, Void.class);
```

#### Find an Article

```java
// Using the application service directly
@Autowired
private FindArticleUseCase findArticleUseCase;

public void findArticle(String articleId) {
    FindArticleQuery query = FindArticleQuery.validateThenCreate(articleId).get();
    Either<Error, ArticleDTO> result = findArticleUseCase.handle(query);
    
    result.fold(
        error -> handleError(error),
        article -> processArticle(article)
    );
}
```

#### Update an Article

```java
// Using the command bus pattern
@Autowired
private CommandBus commandBus;

public void updateArticle(String id, String authorId, String title, String content) {
    UpdateArticleCommand command = UpdateArticleCommand.validateThenCreate(id, authorId, title, content).get();
    Either<Error, Void> result = commandBus.execute(command);
    
    result.fold(
        error -> handleError(error),
        success -> System.out.println("Article updated successfully")
    );
}
```

## 🏗️ Architecture

The application is structured following the Hexagonal Architecture pattern:

### Core Components

#### 🧠 Domain Layer (`application-core/domain`)

- Contains the business entities, value objects, and domain services
- Implements core business rules and logic
- Has no dependencies on external frameworks or libraries

#### 🔄 Application Layer (`application-core/application`)

- Implements use cases as command and query handlers
- Orchestrates the flow of data between the domain and ports
- Contains application-specific business rules

#### 🔌 Input Ports (`application-core/input-ports`)

- Defines interfaces for the primary/driving adapters to use
- Specifies commands and queries that the application can handle
- Provides clear entry points into the application

#### 🔌 Output Ports (`application-core/output-ports`)

- Defines interfaces for the secondary/driven adapters to implement
- Specifies how the application interacts with external systems
- Enables pluggable implementations of infrastructure concerns

### Adapter Components

#### 🌐 API Adapter (`api-adapter`)

- Implements REST controllers as primary adapters
- Translates HTTP requests into application commands and queries
- Handles API-specific concerns like request validation and response formatting

#### 💾 In-Memory Repositories (`in-memory-repositories`)

- Implements repository interfaces as secondary adapters
- Provides in-memory storage for entities
- Demonstrates how to swap different persistence mechanisms

#### 🔄 Author External Adapter (`author-external-adapter`)

- Integrates with external author services
- Translates between external data formats and domain models
- Handles external service communication details

#### 🧩 Spring Boot Assembly (`spring-boot-assembly`)

- Wires all components together using Spring Boot
- Configures dependency injection and application properties
- Provides the runnable application entry point

## ⚙️ How It Works

1. **🔍 Request Handling**: API requests are received by controllers in the API adapter
2. **🎯 Command/Query Creation**: Controllers create commands or queries and pass them to the appropriate use case
3. **🧠 Business Logic**: Application services execute business logic using domain entities
4. **🔌 External Integration**: Output ports are used to interact with external systems when needed
5. **📊 Response Formation**: Results are mapped back to API responses and returned to the client

## 🧪 Testing

The project includes comprehensive tests for all layers:

```bash
# Run all tests
mvn test

# Generate test coverage report
mvn clean test jacoco:report
```

### 📊 Test Coverage

- ✅ **Domain Tests**: Verify business rules and entity behavior
- ✅ **Application Tests**: Ensure use cases work correctly with mocked dependencies
- ✅ **Adapter Tests**: Validate correct interaction with external systems
- ✅ **Integration Tests**: Verify components work together properly
- ✅ **90%+ Coverage**: Enforced by JaCoCo configuration

### 🔧 Test Design Philosophy

- **Given/When/Then**: Clear test structure for readability
- **Isolated Components**: Pure unit tests with mocking
- **Hexagonal Testing**: Each layer tested independently
- **Behavior Verification**: Focus on verifying correct behavior

## 📋 Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| **Spring Boot** | 4.0.1 | Core Spring Boot framework |
| **Java** | 25+ | Runtime platform |
| **Vavr** | 0.11.0 | Functional programming with Either |
| **Lombok** | 1.18.42 | Boilerplate code reduction |
| **MapStruct** | 1.6.3 | Object mapping |
| **Shared Kernel** | 1.0.0 | Common components |

### Test Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| **JUnit Jupiter** | 6.0.2 | Testing framework |
| **Mockito** | 5.21.0 | Mocking framework |
| **AssertJ** | 3.27.6 | Fluent assertions |
| **SLF4J** | 2.0.17 | Logging framework |

## 🔧 Build Requirements

- **Java 25+**
- **Maven 3.9+**
- **Spring Boot 4.0.1+**

### Building

```bash
# Compile the project
mvn clean compile

# Run tests
mvn test

# Package the application
mvn clean package

# Run the application
java -jar spring-boot-assembly/target/spring-boot-assembly-0.0.1.jar
```

## 🤝 Contributing

1. 🍴 Fork the repository
2. 🌿 Create a feature branch (`git checkout -b feature/amazing-feature`)
3. ✅ Add tests for your changes
4. 🧪 Ensure all tests pass (`mvn test`)
5. 📊 Maintain 90%+ test coverage
6. 📝 Update documentation as needed
7. 📤 Submit a pull request

## 📄 License

This project is licensed under the terms of the MIT license.

## 👨‍💻 Author

**Enrique Medina Montenegro**

---

## 🏷️ Tags

`hexagonal-architecture` `spring-boot` `ports-and-adapters` `ddd` `clean-architecture` `java` `vavr` `functional-programming` `reference-implementation` `testing` `spring-framework` `error-handling` `modular-design`

---

*🔷 This reference application demonstrates how to implement a clean, maintainable, and testable application using Hexagonal Architecture principles with the Spring ecosystem.*
