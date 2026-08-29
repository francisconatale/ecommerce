package com.eshop.infrastructure.web;

import com.eshop.domain.product.ProductService;
import com.eshop.infrastructure.web.dto.ProductRequest;
import com.eshop.infrastructure.web.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        log.info("Recibida petición POST para crear producto: {}", request.name());
        return ProductResponse.fromDomain(productService.create(request.name(), request.priceBuy(), request.priceSell(), request.categoryId()));
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        log.info("Recibida petición GET para obtener todos los productos");
        return productService.findAll().stream()
                .map(ProductResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable UUID id) {
        log.info("Recibida petición GET para obtener producto: {}", id);
        return ProductResponse.fromDomain(productService.findById(id));
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(@PathVariable UUID id, @RequestBody ProductRequest request) {
        log.info("Recibida petición PUT para actualizar producto: {}", id);
        return ProductResponse.fromDomain(productService.update(id, request.name(), request.priceBuy(), request.priceSell(), request.categoryId()));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id) {
        log.info("Recibida petición DELETE para borrar producto: {}", id);
        productService.delete(id);
    }
}