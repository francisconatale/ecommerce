package com.eshop.infrastructure.web.category;
import com.eshop.infrastructure.web.base.BaseController;
import com.eshop.infrastructure.web.base.BaseController;

import com.eshop.domain.category.CategoryService;
import com.eshop.infrastructure.web.base.ApiResponse;
import com.eshop.domain.category.CreateCategoryCommand;
import com.eshop.domain.category.UpdateCategoryCommand;

import com.eshop.infrastructure.web.category.CategoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        CreateCategoryCommand command = new CreateCategoryCommand(request.name(), request.parentId());
        CategoryResponse response = CategoryResponse.fromDomain(categoryService.create(command));
        return created(response, response.id().toString());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryResponse>>> getCategories(Pageable pageable) {
        Page<CategoryResponse> categories = categoryService.findAll(pageable)
                .map(CategoryResponse::fromDomain);
                
        if (categories.isEmpty()) {
            return noContent();
        }
        return ok(categories);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable UUID id, @Valid @RequestBody UpdateCategoryRequest request) {
        UpdateCategoryCommand command = new UpdateCategoryCommand(id, request.name(), request.parentId());
        CategoryResponse response = CategoryResponse.fromDomain(categoryService.update(command));
        return ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.delete(id);
        return noContent();
    }
}
