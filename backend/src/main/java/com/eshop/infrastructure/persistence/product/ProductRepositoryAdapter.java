package com.eshop.infrastructure.persistence.product;

import com.eshop.domain.product.Product;
import com.eshop.domain.product.ProductRepository;
import com.eshop.domain.product.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final SpringDataProductRepository repository;
    private final ProductMapper mapper;

    public ProductRepositoryAdapter(SpringDataProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Product> findById(UUID id) {
        log.debug("Buscando producto por ID en DB: {}", id);
        return mapper.toDomain(repository.findById(id));
    }

    @Override
    public Product save(Product product) {
        log.debug("Guardando producto en DB: {}", product.getName());
        ProductEntity entity;
        
        if (product.isNew()) {
            entity = mapper.toEntity(product);
        } else {
            entity = repository.findById(product.getId())
                    .map(existing -> {
                        mapper.updateEntity(product, existing);
                        return existing;
                    })
                    .orElseGet(() -> mapper.toEntity(product));
        }
        
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<Product> findByCategoryId(UUID categoryId) {
        log.debug("Buscando productos por categoria en DB: {}", categoryId);
        return mapper.toDomain(repository.findByCategoryId(categoryId));
    }

    @Override
    public List<Product> findByCategoryAndDescendants(UUID categoryId) {
        log.debug("Buscando productos por categoria y descendientes en DB: {}", categoryId);
        return mapper.toDomain(repository.findByCategoryAndDescendants(categoryId));
    }

    @Override
    public Page<Product> findAll(ProductFilter filter, Pageable pageable) {
        log.debug("Buscando todos los productos en DB (paginado)");
        Specification<ProductEntity> spec = ProductSpecifications.withFilter(filter);
        return repository.findAll(spec, pageable).map(mapper::toDomain);
    }

    @Override
    public void delete(Product product) {
        log.debug("Eliminando producto en DB: {}", product.getId());
        repository.deleteById(product.getId());
    }
}
