<!-- Sync Impact Report
Version change: 1.2.0 -> 1.3.0
Modified principles: II. Domain-Driven Design (DDD) - Enforced Adapters
Added sections: VII. Integration Testing vs Mocks, VIII. Soft Deletes
Removed sections: None
Templates requiring updates: None
Follow-up TODOs: None
-->
# E-Shop MVP Constitution

## Core Principles

### I. Test-Driven Development (TDD)
TDD is mandatory for all core business logic and critical paths. The Red-Green-Refactor cycle MUST be strictly enforced: write a failing test first, make it pass with minimal code, then refactor. Tests must run fast and reliably in isolation.

### II. Domain-Driven Design (DDD) & Adapters
The architecture MUST follow DDD principles, utilizing Hexagonal (Ports and Adapters) or Clean Architecture patterns. The core domain (Entities, Value Objects, Aggregates, Domain Services) MUST remain isolated from infrastructure, databases, and external frameworks (including Spring Boot). Interfaces (Ports) will be used to define contracts for external dependencies. Adapters MUST ALWAYS be created in the Infrastructure layer to implement these interfaces (e.g. mapping Domain Entities to JPA Entities).

### III. Spring Boot Ecosystem
Leverage standard Spring Boot conventions, starters, and configuration for infrastructure and delivery mechanisms (Adapters). Keep the configuration minimal and simple. Avoid tightly coupling the domain layer to Spring specific annotations or beans.

### IV. Simplicity & MVP Focus
Focus strictly on Minimum Viable Product (MVP) requirements. Avoid over-engineering and premature optimization. Apply the YAGNI (You Aren't Gonna Need It) principle continuously to reduce technical debt and complexity.

### V. Observability & Quality
Ensure robust observability through structured logging and clear error handling. Exception handling must clearly distinguish between domain errors and infrastructure failures. The system should provide actionable feedback for debugging.

### VI. Clean Code & Imports
Code MUST NOT use fully qualified class names (literals/FQNs) inline within the code (e.g., `java.util.List`). Standard imports at the top of the file MUST always be used instead to keep code clean, concise, and readable.

### VII. Integration Testing & Testcontainers
Favor Integration Tests over Mocks for business logic involving persistence or critical flows. Tests MUST use Testcontainers to validate real database constraints (NOT NULL, FKs) and ORM mappings (e.g. @PrePersist), which Mockito would otherwise hide.

### VIII. Soft Deletes
Core domain entities (e.g., Categories, Products) MUST utilize Soft Deletes (`deleted` boolean flag) instead of physical database deletion to preserve historical data, reporting capabilities, and referential integrity.

## Architecture Guidelines

The structure will typically include:
- **Domain Layer**: Core business models and rules (No Spring dependencies).
- **Application Layer**: Use cases / Application services coordinating the domain.
- **Infrastructure Layer**: Implementations of repositories via Adapters, external clients (Spring Data, REST clients).
- **Delivery/Web Layer**: REST Controllers (Spring Web).

## Governance

This Constitution supersedes all other practices. All PRs and code reviews MUST verify compliance with these core principles, particularly the strict isolation of the Domain layer and TDD enforcement. Any amendments to this Constitution require documentation and approval via team consensus.

### Feature Documentation Workflow
For every new feature processed, the agent MUST create or update a markdown file in the `documentation/` directory at the root of the `/backend` project. This file must contain a detailed breakdown of the work performed, the decisions made by the user, and the rationale behind those decisions.

**Version**: 1.3.0 | **Ratified**: 2026-08-29 | **Last Amended**: 2026-08-29
