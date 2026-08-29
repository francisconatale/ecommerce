# Implementation Plan: Category Closure Table

**Branch**: `001-category-closure-table` | **Date**: 2026-08-29 | **Spec**: [spec.md](file:///C:/Users/franc/OneDrive/Desktop/prototype-eshop/backend/specs/001-category-closure-table/spec.md)

**Input**: Feature specification from `/specs/001-category-closure-table/spec.md`

## Summary

Implement a hierarchical category model for products using the Closure Table pattern to avoid recursive queries. The solution provides O(1) depth reads for product discovery while maintaining structural integrity (max depth 10, no cycles) through transactional service operations.

## Technical Context

**Language/Version**: Java 17+ (or 21)

**Primary Dependencies**: Spring Boot 3.x, Spring Web, Spring Data JPA

**Storage**: PostgreSQL (or equivalent relational DB that supports standard SQL indices and foreign keys)

**Testing**: JUnit 5, Mockito, Testcontainers (for DB integration tests)

**Target Platform**: Backend Web Service

**Project Type**: Web Service

**Performance Goals**: O(1) query time for reading a category tree and finding products.

**Constraints**: Tree depth strictly limited to 10. Operations must prevent circular references.

**Scale/Scope**: E-commerce catalog tree, suitable for MVP but scalable to thousands of categories.

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **TDD (I)**: The closure table logic is complex. We will start with failing tests for `CategoryService` (move, delete, insert) before implementing the tree logic.
- **DDD (II)**: We will create a `Category` entity and a `ClosureTable` logic in the Domain layer, while keeping Spring dependencies in the Infrastructure layer.
- **Simplicity (IV)**: Only implementing the required closure table methods, without creating a fully generalized graph library.
- **Observability (V)**: The exceptions thrown during tree operations (e.g. cycle detected, max depth exceeded) must have clear, distinct error messages.

## Project Structure

### Documentation (this feature)

```text
specs/001-category-closure-table/
├── plan.md              
├── research.md          
├── data-model.md        
├── quickstart.md        
└── contracts/           
```

### Source Code (repository root)

```text
src/
├── main/
│   └── java/com/eshop/
│       ├── domain/
│       │   └── category/
│       │       ├── Category.java
│       │       ├── CategoryService.java
│       │       └── exceptions/
│       ├── application/
│       │   └── CategoryUseCase.java
│       ├── infrastructure/
│       │   ├── persistence/
│       │   │   ├── CategoryEntity.java
│       │   │   ├── CategoryClosureEntity.java
│       │   │   └── SpringDataCategoryRepository.java
│       │   └── web/
│       │       └── CategoryController.java
└── test/
    └── java/com/eshop/
        ├── domain/
        └── infrastructure/
```

**Structure Decision**: Hexagonal Architecture grouping by feature (category). The domain layer contains the pure logic, infrastructure contains Spring Data repositories and REST controllers.

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

No violations. Closure table complexity is justified by the requirement to avoid recursive queries.
