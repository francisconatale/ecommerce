package com.eshop.domain.product;

import java.util.UUID;
import java.util.Optional;
import java.util.List;
import com.eshop.domain.exception.ResourceNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
    Optional<Product> findById(UUID id);
    Product save(Product product);
    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByCategoryAndDescendants(UUID categoryId);
    Page<Product> findAll(Pageable pageable);
    void delete(Product product);

    default Product getOrThrow(UUID id) {
        return findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }
}
