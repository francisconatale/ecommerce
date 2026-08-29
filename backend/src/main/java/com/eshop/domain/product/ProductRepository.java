package com.eshop.domain.product;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

public interface ProductRepository {
    Optional<Product> findById(UUID id);
    Product save(Product product);
    List<Product> findByCategoryId(UUID categoryId);
    List<Product> findByCategoryAndDescendants(UUID categoryId);
    List<Product> findAll();
    void delete(Product product);
}
