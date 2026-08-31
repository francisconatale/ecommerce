package com.eshop.infrastructure.web.product;
import com.eshop.infrastructure.web.base.BaseController;
import com.eshop.infrastructure.web.base.BaseController;

import com.eshop.domain.product.ProductService;
import com.eshop.infrastructure.web.base.ApiResponse;
import com.eshop.domain.product.CreateProductCommand;
import com.eshop.domain.product.UpdateProductCommand;

import com.eshop.infrastructure.web.product.ProductResponse;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductService productService;
    private final ProductWebMapper productWebMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        CreateProductCommand command = new CreateProductCommand(request.name(), request.priceBuy(), request.priceSell(), request.categoryId());
        ProductResponse response = productWebMapper.toResponse(productService.create(command));
        return created(response, response.id().toString());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProducts(Pageable pageable) {
        Page<ProductResponse> products = productService.findAll(pageable)
                .map(productWebMapper::toResponse);
                
        if (products.isEmpty()) {
            return noContent();
        }
        return ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable UUID id) {
        ProductResponse response = productWebMapper.toResponse(productService.findById(id));
        return ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable UUID id, @Valid @RequestBody UpdateProductRequest request) {
        UpdateProductCommand command = new UpdateProductCommand(id, request.name(), request.priceBuy(), request.priceSell(), request.categoryId());
        ProductResponse response = productWebMapper.toResponse(productService.update(command));
        return ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.delete(id);
        return noContent();
    }
}
