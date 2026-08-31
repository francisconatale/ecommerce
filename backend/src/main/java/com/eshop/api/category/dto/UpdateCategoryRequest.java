package com.eshop.api.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UpdateCategoryRequest(
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    String name,
    UUID parentId // null para las raices
) {}
