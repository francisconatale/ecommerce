package com.eshop.infrastructure.web;

import com.eshop.domain.category.Category;
import com.eshop.domain.category.CategoryService;
import com.eshop.infrastructure.persistence.SpringDataProductRepository;
import com.eshop.infrastructure.persistence.SpringDataProductRepository.ProductWithBreadcrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private SpringDataProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories")
    public void createCategory(@RequestBody Category category) {
        // En un proyecto final esto usaría DTOs en lugar de la clase de dominio directo.
        categoryService.create(category.getName(), category.getParentId());
    }

    @PutMapping("/products/{productId}/category/{categoryId}")
    public void assignProductToCategory(@PathVariable UUID productId, @PathVariable UUID categoryId) {
        // T012: Assign product to leaf category
        categoryService.assignProduct(productId, categoryId);
    }

    // T015: Endpoint to discover products efficiently without N+1
    @GetMapping("/categories/{id}/products")
    public List<ProductWithBreadcrumb> getProductsByCategory(@PathVariable UUID id) {
        return productRepository.findProductsByCategoryDescendantsWithBreadcrumb(id);
    }
}
