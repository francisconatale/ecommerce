package com.eshop.infrastructure.web.category;

import com.eshop.domain.category.Category;
import java.util.UUID;

public record CategoryResponse(UUID id, String name, UUID parentId, String pathNames) {
    public static CategoryResponse fromDomain(Category category) {
        return new CategoryResponse(
            category.getId(),
            category.getName(),
            category.getParentId(),
            category.getPathNames()
        );
    }
}