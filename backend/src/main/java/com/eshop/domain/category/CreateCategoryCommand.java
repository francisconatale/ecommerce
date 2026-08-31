package com.eshop.domain.category;

import java.util.UUID;

public record CreateCategoryCommand(String name, UUID parentId) {
}
