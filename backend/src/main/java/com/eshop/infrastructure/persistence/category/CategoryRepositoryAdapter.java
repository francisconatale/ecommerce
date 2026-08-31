package com.eshop.infrastructure.persistence.category;

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
    private final CategoryMapper mapper;

    public CategoryRepositoryAdapter(SpringDataCategoryRepository repository, SpringDataCategoryClosureRepository closureRepository, CategoryMapper mapper) {
        this.repository = repository;
        this.closureRepository = closureRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Category> findById(UUID id) {
        log.debug("Buscando categoría por ID en DB: {}", id);
        return mapper.toDomain(repository.findById(id));
    }

    @Override
    public Category save(Category category) {
        log.debug("Guardando categoría en DB: {}", category.getName());
        CategoryEntity entity;
        
        if (category.isNew()) {
            entity = mapper.toEntity(category);
        } else {
            entity = repository.findById(category.getId())
                    .map(existing -> {
                        mapper.updateEntity(category, existing);
                        return existing;
                    })
                    .orElseGet(() -> mapper.toEntity(category));
        }
        
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<Category> findDescendants(UUID categoryId) {
        log.debug("Buscando descendientes de categoría en DB: {}", categoryId);
        return mapper.toDomain(repository.findDescendants(categoryId));
    }

    @Override
    public org.springframework.data.domain.Page<Category> findAll(org.springframework.data.domain.Pageable pageable) {
        log.debug("Buscando todas las categorías en DB (paginado)");
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void delete(Category category) {
        log.debug("Eliminando categoría en DB: {}", category.getId());
        repository.deleteById(category.getId());
    }
}
