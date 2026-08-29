# Quickstart & Validation Guide

This guide describes how to validate the Category Closure Table feature once implemented.

## Prerequisites
- Java 17+ installed
- Docker (for Testcontainers)
- Maven or Gradle wrapper (assuming `mvnw`)

## Running the Application
1. Start the application locally:
   ```bash
   ./mvnw spring-boot:run
   ```
2. The application should automatically execute Flyway/Liquibase migrations to create the schema and the default "Uncategorized" category.

## End-to-End Validation Scenarios

You can use `curl` or Postman to validate the feature against the contracts defined in `contracts/category-api.md`.

### 1. Test Depth and Move
1. Create a root category "A".
2. Create category "B" under "A".
3. Create category "C" under "B".
4. Move "C" to be under "A" (making it a sibling of "B").
5. The API should return 200 OK.

### 2. Test Cycle Prevention
1. Create "A" -> "B" -> "C".
2. Attempt to move "A" under "C" (`PUT /api/categories/{A_ID}/move` with `newParentId = C_ID`).
3. The API should return `400 Bad Request` with a clear message indicating a cycle was detected.

### 3. Test Product Discovery
1. Create hierarchy: Electronics -> Computers -> Laptops.
2. Assign a product "MacBook Pro" to the leaf category "Laptops".
3. Query `GET /api/categories/{Electronics_ID}/products`.
4. The response should include "MacBook Pro" without requiring multiple recursive queries to the DB.

### 4. Test Deletion Inheritance
1. Create hierarchy: "Parent" -> "Child".
2. Assign product "Toy" to "Child".
3. Delete "Child" (`DELETE /api/categories/{Child_ID}`).
4. Query `GET /api/categories/{Parent_ID}/products`.
5. "Toy" should now be directly under "Parent".

## Running Tests
Run the integration tests (which spin up a real PostgreSQL database):
```bash
./mvnw test
```
Ensure all Domain tests (validations, rules) and Infrastructure tests (Closure Table SQL queries) pass.
