package com.eshop.infrastructure.web;

import com.eshop.domain.category.CategoryService;
import com.eshop.infrastructure.web.dto.ApiResponse;
import com.eshop.infrastructure.web.dto.CategoryRequest;
import com.eshop.infrastructure.web.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest request) {
        log.info("Recibida petición POST para crear categoría: {}", request.name());
        CategoryResponse response = CategoryResponse.fromDomain(categoryService.create(request.name(), request.parentId()));
        return ResponseEntity
                .created(URI.create("/api/categories/" + response.id()))
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        log.info("Recibida petición GET para obtener todas las categorías");
        List<CategoryResponse> categories = categoryService.findAll().stream()
                .map(CategoryResponse::fromDomain)
                .collect(Collectors.toList());
                
        if (categories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable UUID id, @RequestBody CategoryRequest request) {
        log.info("Recibida petición PUT para actualizar categoría {}", id);
        CategoryResponse response = CategoryResponse.fromDomain(categoryService.update(id, request.name(), request.parentId()));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        log.info("Recibida petición DELETE para borrar categoría {}", id);
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}