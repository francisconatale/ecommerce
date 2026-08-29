package com.eshop.domain.category;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

public interface CategoryRepository {
    Optional<Category> findById(UUID id);
    Category save(Category category);
    List<Category> findDescendants(UUID categoryId);
    List<Category> findAll();
    void delete(Category category);
}
