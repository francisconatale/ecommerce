# Phase 1: Data Model

## Conceptual Entities

### Entity Base
Abstract properties shared across all entities.
- `uuid` (UUID, Primary Key)
- `created_at` (Timestamp)
- `updated_at` (Timestamp)
- `deleted` (Boolean, for soft deletes)

### Category
Represents a node in the product catalog tree.
- `id`: Inherited `uuid`
- `name`: String
- `parent_id`: UUID (Nullable, FK to self)
- `is_system`: Boolean (Used to protect the default "Uncategorized" category)
- `path_names`: String (Cached breadcrumb text, e.g., "Tecnología > Computadora > Laptop")

### Category Closure
Maintains the paths between all ancestors and descendants in the tree.
- `ancestor_id`: UUID (FK to Category)
- `descendant_id`: UUID (FK to Category)
- `depth`: Integer (0 for self-reference, >0 for ancestors)

**Indexes:**
- PK: `(ancestor_id, descendant_id)`
- Index: `idx_closure_descendant (descendant_id)`
- Index: `idx_closure_depth (ancestor_id, depth)`

### Product
An item in the catalog.
- `id`: Inherited `uuid`
- `name`: String
- `price_buy`: Decimal
- `price_sell`: Decimal
- `category_id`: UUID (FK to Category, must point to a leaf node)

**Indexes:**
- Index: `idx_product_category (category_id)`

## State Transitions & Rules

- **Updating Names/Paths**: Whenever a category is created, renamed, or moved, the system MUST recalculate and update the `path_names` column for that category and ALL of its descendants.
- **Moving a Category**: 
  1. Validates the `newParentId` is not a descendant of the moved node (cycle prevention).
  2. Validates new depth does not exceed 10.
  3. Updates `parent_id`.
  4. Deletes old closure records pointing outside the moved subtree.
  5. Inserts new closure records mapping the subtree to the new ancestors.
  6. Recalculates `path_names` for the subtree.
- **Deleting a Category**:
  1. Promotes all immediate child categories to the deleted category's `parent_id` (or the system default if `parent_id` was null).
  2. Reassigns all products to the `parent_id` (or system default).
  3. Deletes closure records referencing this category.
  4. Flags category as `deleted = true`.
  5. Recalculates `path_names` for the promoted child categories and their descendants.
- **System Category**: The default category `is_system = true` cannot be deleted or moved.
