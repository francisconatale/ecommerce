package com.eshop.infrastructure.web;

import com.eshop.domain.category.CategoryService;
import com.eshop.infrastructure.web.dto.CategoryRequest;
import com.eshop.infrastructure.web.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategoryResponse createCategory(@RequestBody CategoryRequest request) {
        log.info("Recibida petición POST para crear categoría: {}", request.name());
        return CategoryResponse.fromDomain(categoryService.create(request.name(), request.parentId()));
    }

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        log.info("Recibida petición GET para obtener todas las categorías");
        return categoryService.findAll().stream()
                .map(CategoryResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable UUID id, @RequestBody CategoryRequest request) {
        log.info("Recibida petición PUT para actualizar categoría {}", id);
        return CategoryResponse.fromDomain(categoryService.update(id, request.name(), request.parentId()));
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable UUID id) {
        log.info("Recibida petición DELETE para borrar categoría {}", id);
        categoryService.delete(id);
    }
}