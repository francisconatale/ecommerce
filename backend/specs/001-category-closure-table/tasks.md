---
description: "Task list for Hierarchical Categories Implementation"
---

# Tasks: Category Closure Table

**Input**: Design documents from `/specs/001-category-closure-table/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/, quickstart.md
**Constitution**: TDD Mandatory (Tests must be written first)

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic Spring Boot structure

- [x] T001 Initialize Java 17+ Spring Boot 3.x project (Web, JPA, Flyway, PostgreSQL)
- [x] T002 [P] Configure Testcontainers for PostgreSQL in test application properties

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

- [x] T003 Create Flyway migration V1 for `category`, `category_closure`, and `product` tables. Includes `is_system` and `path_names` on `category`.
- [x] T004 Create base JPA Entities (`CategoryEntity.java`, `CategoryClosureEntity.java`, `ProductEntity.java`) in `src/main/java/com/eshop/infrastructure/persistence/`
- [x] T005 [P] Create empty Domain models (`Category.java`, `Product.java`) in `src/main/java/com/eshop/domain/`
- [x] T006 Create System Default "Uncategorized" category in a Flyway V2 migration.

**Checkpoint**: Foundation ready - Database schema and base classes are present.

---

## Phase 3: User Story 1 - Categorize Products (Priority: P1) 🎯 MVP

**Goal**: Assign a product to a specific leaf category.

**Independent Test**: Can be fully tested by creating a leaf category and assigning a new product to it.

### Tests for User Story 1 (TDD) ⚠️

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [x] T007 [P] [US1] Integration test for assigning product to category in `src/test/java/com/eshop/application/CategorizeProductUseCaseTest.java` (using Testcontainers)
- [x] T008 [P] [US1] Unit test for leaf validation in `src/test/java/com/eshop/domain/category/CategoryTest.java`

### Implementation for User Story 1

- [x] T009 [P] [US1] Implement `CategoryRepository` interface in domain and `SpringDataCategoryRepository` in infrastructure.
- [x] T010 [P] [US1] Implement `ProductRepository` interface and its infrastructure implementation.
- [x] T011 [US1] Implement Domain logic to validate product assignment (only to leaf nodes, if required, though relaxed on deletion) in `src/main/java/com/eshop/domain/category/CategoryService.java`
- [x] T012 [US1] Implement `POST /api/categories` and `PUT /api/products/{productId}/category` in `CategoryController.java`

**Checkpoint**: At this point, basic categories can be created and products can be assigned to them.

---

## Phase 4: User Story 2 - Discover Products by Category (Priority: P1)

**Goal**: Retrieve all products within a specific category, including all its subcategories efficiently (O(1) read).

**Independent Test**: Query a top-level category to ensure all descendant products are returned.

### Tests for User Story 2 (TDD) ⚠️

- [x] T013 [P] [US2] Integration test for product discovery (`GET /api/categories/{id}/products`) ensuring closure table is used in `src/test/java/com/eshop/infrastructure/web/CategoryControllerIntegrationTest.java`

### Implementation for User Story 2

- [x] T014 [US2] Implement JPA query in `SpringDataCategoryRepository` joining `ProductEntity`, `CategoryEntity`, and `CategoryClosureEntity` to fetch all descendant products.
- [x] T015 [US2] Implement `GET /api/categories/{id}/products` endpoint returning products and the cached `path_names` (breadcrumb).

**Checkpoint**: Product discovery with breadcrumbs works using the Closure Table structure.

---

## Phase 5: User Story 3 - Reorganize Categories (Priority: P2)

**Goal**: Move and delete categories, maintaining tree integrity (prevent cycles, depth limit, inherited products).

**Independent Test**: Move a node and verify cycle prevention. Delete an intermediate node and verify children/products inherit to the parent.

### Tests for User Story 3 (TDD) ⚠️

- [x] T016 [P] [US3] Unit tests for Cycle Prevention, Depth Limit (max 10) in `CategoryServiceTest.java`
- [x] T017 [P] [US3] Integration test for Closure Table math (Move Category) in `CategoryRepositoryTest.java`
- [x] T018 [P] [US3] Integration test for Deletion Inheritance and `path_names` cache recalculation.

### Implementation for User Story 3

- [x] T019 [US3] Implement `moveCategory` in `CategoryService.java` (validates cycles, limits depth).
- [x] T020 [US3] Implement Closure Table update logic in Infrastructure (delete old ancestors, insert new ancestors) transactionally.
- [x] T021 [US3] Implement logic to recursively update `path_names` caching column for moved/renamed subtrees.
- [x] T022 [US3] Implement `deleteCategory` (promotes children and products to parent, recalculates `path_names`).
- [x] T023 [US3] Implement `PUT /api/categories/{id}/move` and `DELETE /api/categories/{id}` in Controller.

**Checkpoint**: All hierarchical reorganization rules, validations, and the closure math are complete.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [x] T024 [P] Refactor exception handling in `GlobalExceptionHandler.java` to return clear 400 messages for Domain errors (Cycle detected, Max depth exceeded).
- [x] T025 Run all scenarios defined in `quickstart.md` manually to validate end-to-end functionality.

---

## Dependencies & Execution Order

### Phase Dependencies
- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion

### Within Each User Story
- Tests MUST be written and FAIL before implementation (TDD).
- Infrastructure/Domain interfaces before Business Logic.
- Domain logic before REST Controllers.

### Parallel Opportunities
- Foundational JPA entities (T004) and Domain Models (T005) can run in parallel.
- Tests (T016, T017, T018) can be written in parallel by different developers.
- `CategoryRepository` (T009) and `ProductRepository` (T010) implementations can run in parallel.
