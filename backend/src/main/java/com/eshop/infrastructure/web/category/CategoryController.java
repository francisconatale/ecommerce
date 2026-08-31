package com.eshop.infrastructure.web.category;
import com.eshop.infrastructure.web.base.BaseController;
import com.eshop.infrastructure.web.base.BaseController;

import com.eshop.domain.category.CategoryService;
import com.eshop.infrastructure.web.base.ApiResponse;
import com.eshop.infrastructure.web.category.CategoryRequest;
import com.eshop.infrastructure.web.category.CategoryResponse;
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
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse response = CategoryResponse.fromDomain(categoryService.create(request.name(), request.parentId()));
        return created(response, "/api/categories/" + response.id());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.findAll().stream()
                .map(CategoryResponse::fromDomain)
                .collect(Collectors.toList());
                
        if (categories.isEmpty()) {
            return noContent();
        }
        return ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable UUID id, @RequestBody CategoryRequest request) {
        CategoryResponse response = CategoryResponse.fromDomain(categoryService.update(id, request.name(), request.parentId()));
        return ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.delete(id);
        return noContent();
    }
}
