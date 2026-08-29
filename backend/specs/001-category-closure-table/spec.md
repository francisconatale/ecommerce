# Feature Specification: Hierarchical Categories

**Feature Branch**: `001-category-closure-table`

**Created**: 2026-08-29

**Status**: Draft

**Input**: User description: "Planificación: Modelo de Categorías Jerárquicas con Closure Table..."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Categorize Products (Priority: P1)

As a product manager, I need to assign a product to a specific subcategory so that it can be found efficiently in the catalog.

**Why this priority**: Essential for basic catalog structure and organization.

**Independent Test**: Can be fully tested by creating a leaf category and assigning a new product to it, validating the product shows up when that category is viewed.

**Acceptance Scenarios**:

1. **Given** a system with categories, **When** I assign a product to a category, **Then** the product must be assigned to the most specialized category (a leaf node) and never an intermediate node.

---

### User Story 2 - Discover Products by Category (Priority: P1)

As a customer or system client, I want to retrieve all products within a specific category, including all its subcategories, so that I can browse broad sections of the catalog efficiently.

**Why this priority**: Core value of the hierarchical structure; without it, navigating the tree has no practical value for product discovery.

**Independent Test**: Can be tested by creating a tree of categories, assigning products to leaf nodes, and querying a top-level category to ensure all descendant products are returned.

**Acceptance Scenarios**:

1. **Given** a category with multiple nested subcategories containing products, **When** I request products for the parent category, **Then** I receive a list of all products belonging to the parent and any of its descendants.

---

### User Story 3 - Reorganize Categories (Priority: P2)

As an administrator, I need to create, move, and delete categories so that I can maintain an up-to-date catalog hierarchy without breaking existing product assignments.

**Why this priority**: Needed for long-term maintenance of the catalog tree.

**Independent Test**: Can be tested by creating a subtree, moving a node to another parent, and verifying that the structure and product visibility correctly update.

**Acceptance Scenarios**:

1. **Given** an existing category subtree, **When** I move a category to a new parent, **Then** the entire subtree must move without creating cyclical references (infinite loops).
2. **Given** an existing category with products/subcategories, **When** I delete the category, **Then** its contents must inherit the deleted category's parent (or a system default if it was a root node).

### Edge Cases

- What happens when a user attempts to create a cyclical category relationship (e.g., making a parent a child of its own child)? The system must reject the operation.
- What happens when a category tree becomes too deep? The system enforces a maximum depth of 10 levels to prevent extreme hierarchy depth.
- What happens when a root category is deleted? The orphaned products and subcategories are reassigned to a protected "Uncategorized" (default system) category.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST enforce that products are assigned to leaf categories (categories with no children) during creation or reassignment. (Note: A parent node may temporarily inherit products if a leaf is deleted, but new assignments must be to leaves).
- **FR-002**: System MUST support retrieving all products for a given category and all its descending subcategories efficiently.
- **FR-003**: System MUST prevent creating cyclical dependencies when moving or creating categories.
- **FR-004**: System MUST limit category tree depth to a maximum of 10 levels.
- **FR-005**: System MUST ensure that deleting a category promotes its children and products to its immediate parent.
- **FR-006**: System MUST ensure that deleting a root category promotes its children and products to a protected default system category ("Uncategorized").
- **FR-007**: System MUST NOT allow the deletion of the default system category.
- **FR-008**: System MUST support multiple root categories (a forest structure).

### Key Entities

- **Category**: A hierarchical node representing a catalog grouping. Can have one parent (or no parent for root categories).
- **Product**: A catalog item that is associated with a category.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Moving a category subtree with hundreds of nodes completes successfully without compromising data integrity.
- **SC-002**: Retrieving products for a root category with deep subcategories returns results quickly without requiring recursive querying (e.g., N+1 queries).
- **SC-003**: 100% protection against circular category references.

## Assumptions

- No specialized locking is needed for concurrent category editing as trees are implicitly scoped by tenant/user and concurrent conflicting edits are rare.
- Historical audit trailing for category moves is not required for the MVP; standard `updated_at` timestamps suffice.
- The system default "Uncategorized" category will be created during system initialization or migrations.
