package com.eshop.infrastructure.web.product;
import com.eshop.infrastructure.web.base.BaseController;
import com.eshop.infrastructure.web.base.BaseController;

import com.eshop.domain.product.ProductService;
import com.eshop.infrastructure.web.base.ApiResponse;
import com.eshop.infrastructure.web.product.ProductRequest;
import com.eshop.infrastructure.web.product.ProductResponse;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductRequest request) {
        ProductResponse response = ProductResponse.fromDomain(productService.create(request.name(), request.priceBuy(), request.priceSell(), request.categoryId()));
        return created(response, "/api/products/" + response.id());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.findAll().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());
                
        if (products.isEmpty()) {
            return noContent();
        }
        return ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable UUID id) {
        ProductResponse response = ProductResponse.fromDomain(productService.findById(id));
        return ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable UUID id, @RequestBody ProductRequest request) {
        ProductResponse response = ProductResponse.fromDomain(productService.update(id, request.name(), request.priceBuy(), request.priceSell(), request.categoryId()));
        return ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.delete(id);
        return noContent();
    }
}
