package com.eshop.domain.category;

import java.util.UUID;
import java.util.Optional;
import java.util.List;
import com.eshop.domain.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepository {
    Optional<Category> findById(UUID id);
    Category save(Category category);
    List<Category> findDescendants(UUID categoryId);
    Page<Category> findAll(Pageable pageable);
    void delete(Category category);

    default Category getOrThrow(UUID id) {
        return findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}
