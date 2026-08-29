package com.eshop.infrastructure.persistence;

import com.eshop.domain.category.Category;
import com.eshop.domain.category.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final SpringDataCategoryRepository repository;
    private final SpringDataCategoryClosureRepository closureRepository;

    public CategoryRepositoryAdapter(SpringDataCategoryRepository repository, SpringDataCategoryClosureRepository closureRepository) {
        this.repository = repository;
        this.closureRepository = closureRepository;
    }

    @Override
    public Optional<Category> findById(UUID id) {
        log.debug("Buscando categoría por ID en DB: {}", id);
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Category save(Category category) {
        log.debug("Guardando categoría en DB: {}", category.getName());
        CategoryEntity entity = toEntity(category);
        entity = repository.save(entity);
        return toDomain(entity);
    }

    @Override
    public List<Category> findDescendants(UUID categoryId) {
        log.debug("Buscando descendientes de categoría en DB: {}", categoryId);
        return repository.findDescendants(categoryId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findAll() {
        log.debug("Buscando todas las categorías en DB");
        return repository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Category category) {
        log.debug("Eliminando categoría en DB: {}", category.getId());
        repository.deleteById(category.getId());
    }

    private Category toDomain(CategoryEntity entity) {
        Category domain = new Category();
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setParentId(entity.getParentId());
        domain.setPathNames(entity.getPathNames());
        domain.setSystem(entity.isSystem());
        return domain;
    }

    private CategoryEntity toEntity(Category domain) {
        CategoryEntity entity = null;
        if (domain.getId() != null) {
            entity = repository.findById(domain.getId()).orElse(null);
        }
        if (entity == null) {
            entity = new CategoryEntity();
            entity.setId(domain.getId() == null ? UUID.randomUUID() : domain.getId());
        }
        entity.setName(domain.getName());
        entity.setParentId(domain.getParentId());
        entity.setPathNames(domain.getPathNames());
        entity.setSystem(domain.isSystem());
        return entity;
    }
}
