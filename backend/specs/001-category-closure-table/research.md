# Phase 0: Research & Clarifications

## Database Choice
- **Decision**: PostgreSQL.
- **Rationale**: Relational databases are best for the Closure Table pattern as it relies heavily on standard JOINs and foreign keys. PostgreSQL is the standard choice for Spring Boot applications and supports robust indexing and transactions required by the maintenance algorithms (insert, move, delete).
- **Alternatives considered**: MySQL (also viable, but PostgreSQL is often preferred for modern Spring Boot microservices). NoSQL (rejected because it would require custom graphing logic instead of simple SQL JOINs).

## Hexagonal Architecture Mapping for Closure Table
- **Decision**: The actual database tables (`category` and `category_closure`) will be managed by Spring Data JPA in the Infrastructure layer. The Domain layer will define an interface `CategoryRepository` that abstracts the tree operations.
- **Rationale**: Keeps the Domain clean from `@Entity` and Spring annotations, adhering strictly to Principle II (DDD) of the Constitution. The Domain service (`CategoryService`) will handle the business validations (e.g. max depth, cycles) and orchestrate the repository calls.
- **Alternatives considered**: Standard Spring Boot MVC (rejected due to DDD Constitution rule).

## Testing Strategy
- **Decision**: Testcontainers for integration tests of the `SpringDataCategoryRepository`.
- **Rationale**: The core complexity of the Closure Table lies in the SQL queries (moving a subtree, deleting nodes, recalculating depth). Mocking the database would not validate the actual SQL logic. Testcontainers provides a real PostgreSQL instance to ensure the queries work exactly as designed.
- **Alternatives considered**: H2 in-memory DB (rejected because SQL syntax/behavior differences can hide bugs).
