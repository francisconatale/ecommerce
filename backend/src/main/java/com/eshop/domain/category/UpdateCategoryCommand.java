package com.eshop.domain.category;

import java.util.UUID;

public record UpdateCategoryCommand(UUID categoryId, String newName, UUID newParentId) {
}
