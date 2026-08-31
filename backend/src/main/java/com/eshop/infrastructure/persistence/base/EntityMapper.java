package com.eshop.infrastructure.persistence.base;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface EntityMapper<D, E> {

    D toDomain(E entity);

    E toEntity(D domain);
    
    void updateEntity(D domain, @org.mapstruct.MappingTarget E entity);

    default List<D> toDomain(List<E> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    default Optional<D> toDomain(Optional<E> entityOptional) {
        return entityOptional == null ? Optional.empty() : entityOptional.map(this::toDomain);
    }
}
