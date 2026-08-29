# Prototype E-Shop

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-orange)

A scalable, backend-first E-Commerce MVP. Built with a strict separation of concerns utilizing Hexagonal Architecture, ensuring the domain remains completely agnostic of the underlying framework and persistence layers.

## Features

- **Hierarchical Catalog:** Categories are modeled using a Closure Table, allowing for ultra-fast infinite depth querying and safe category reparenting.
- **Data Integrity:** Integration tests run on ephemeral PostgreSQL instances via Testcontainers. 
- **Hexagonal Architecture:** Domain entities are isolated. Infrastructure dependencies (Spring Data, DBs) communicate strictly through Adapters.
- **Soft Deletion:** Core entities (Products, Categories) use logical deletion (`deleted = true`) to preserve reporting and invoicing history.

## Tech Stack

- **Core:** Java 17, Spring Boot 4.1.1 (WebMVC, Data JPA)
- **Database:** PostgreSQL, Flyway (Migrations)
- **Testing:** JUnit 5, Testcontainers
- **Utilities:** Lombok

## Prerequisites

- JDK 17
- Docker (Required for Testcontainers)
- Maven (or use the provided wrapper)

## Running Locally

We recommend using Docker Compose to spin up the backend and database simultaneously.

1. Clone the repository
2. From the root directory, build and run the services:
   ```bash
   docker-compose up --build
   ```
*(Alternatively, you can run the PostgreSQL database in Docker and launch the Spring Boot app natively using `./mvnw spring-boot:run` in the `backend` directory).*

## 📖 API Documentation (Swagger UI)

The backend exposes an auto-generated, interactive OpenAPI specification using **Springdoc**. 
Once the application is running, you can explore the API endpoints, view schemas, and execute test requests directly from your browser:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Testing

The test suite runs integration tests against a real PostgreSQL instance to validate constraints and closure table queries.

```bash
cd backend
./mvnw clean test
```

## Project Structure

```text
backend/
├── src/main/java/com/eshop/
│   ├── domain/           # Pure Java business logic (Entities, Interfaces)
│   ├── application/      # Use cases coordinating the domain
│   └── infrastructure/   # Spring Boot Adapters, JPA Entities, REST Controllers
├── src/main/resources/
│   └── db/migration/     # Flyway SQL schemas
└── documentation/        # Architecture Decision Records (ADRs)
```
