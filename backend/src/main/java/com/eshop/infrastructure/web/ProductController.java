package com.eshop.infrastructure.web;

import com.eshop.domain.product.ProductService;
import com.eshop.infrastructure.web.dto.ApiResponse;
import com.eshop.infrastructure.web.dto.ProductRequest;
import com.eshop.infrastructure.web.dto.ProductResponse;
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
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@RequestBody ProductRequest request) {
        log.info("Recibida petición POST para crear producto: {}", request.name());
        ProductResponse response = ProductResponse.fromDomain(productService.create(request.name(), request.priceBuy(), request.priceSell(), request.categoryId()));
        return ResponseEntity
                .created(URI.create("/api/products/" + response.id()))
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        log.info("Recibida petición GET para obtener todos los productos");
        List<ProductResponse> products = productService.findAll().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());
                
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable UUID id) {
        log.info("Recibida petición GET para obtener producto: {}", id);
        ProductResponse response = ProductResponse.fromDomain(productService.findById(id));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable UUID id, @RequestBody ProductRequest request) {
        log.info("Recibida petición PUT para actualizar producto: {}", id);
        ProductResponse response = ProductResponse.fromDomain(productService.update(id, request.name(), request.priceBuy(), request.priceSell(), request.categoryId()));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        log.info("Recibida petición DELETE para borrar producto: {}", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}