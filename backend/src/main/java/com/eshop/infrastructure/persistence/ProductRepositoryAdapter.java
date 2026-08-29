package com.eshop.infrastructure.persistence;

import com.eshop.domain.product.Product;
import com.eshop.domain.product.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final SpringDataProductRepository repository;

    public ProductRepositoryAdapter(SpringDataProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        entity = repository.save(entity);
        return toDomain(entity);
    }

    @Override
    public List<Product> findByCategoryId(UUID categoryId) {
        return repository.findByCategoryId(categoryId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Product toDomain(ProductEntity entity) {
        Product domain = new Product();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setPriceBuy(entity.getPriceBuy());
        domain.setPriceSell(entity.getPriceSell());
        domain.setCategoryId(entity.getCategoryId());
        return domain;
    }

    private ProductEntity toEntity(Product domain) {
        ProductEntity entity = null;
        if (domain.getId() != null) {
            entity = repository.findById(domain.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new ProductEntity();
            entity.setId(domain.getId() == null ? UUID.randomUUID() : domain.getId());
        }
        entity.setName(domain.getName());
        entity.setPriceBuy(domain.getPriceBuy());
        entity.setPriceSell(domain.getPriceSell());
        entity.setCategoryId(domain.getCategoryId());
        return entity;
    }
}
