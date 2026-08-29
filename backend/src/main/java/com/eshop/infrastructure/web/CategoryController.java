package com.eshop.infrastructure.web;

import com.eshop.domain.category.Category;
import com.eshop.domain.category.CategoryService;
import com.eshop.infrastructure.persistence.SpringDataProductRepository;
import com.eshop.infrastructure.persistence.SpringDataProductRepository.ProductWithBreadcrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private SpringDataProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories")
    public void createCategory(@RequestBody Category category) {
        log.info("Recibida petición POST para crear categoría: {}", category.getName());
        categoryService.create(category.getName(), category.getParentId());
        log.info("Petición POST procesada exitosamente para categoría: {}", category.getName());
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        log.info("Recibida petición GET para obtener todas las categorías");
        return categoryService.findAll();
    }

    @PutMapping("/products/{productId}/category/{categoryId}")
    public void assignProductToCategory(@PathVariable UUID productId, @PathVariable UUID categoryId) {
        log.info("Recibida petición PUT para asignar producto {} a categoría {}", productId, categoryId);
        categoryService.assignProduct(productId, categoryId);
    }

    // T015: Endpoint to discover products efficiently without N+1
    @GetMapping("/categories/{id}/products")
    public List<ProductWithBreadcrumb> getProductsByCategory(@PathVariable UUID id) {
        log.info("Recibida petición GET para obtener productos de la categoría {}", id);
        List<ProductWithBreadcrumb> result = productRepository.findProductsByCategoryDescendantsWithBreadcrumb(id);
        log.info("Retornando {} productos para la categoría {}", result.size(), id);
        return result;
    }
}
