package com.eshop.infrastructure.web;

import com.eshop.domain.product.Product;
import com.eshop.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        log.info("Recibida petición POST para crear producto: {}", product.getName());
        return productService.create(product.getName(), product.getPriceBuy(), product.getPriceSell(), product.getCategoryId());
    }

    @GetMapping
    public List<Product> getAllProducts() {
        log.info("Recibida petición GET para obtener todos los productos");
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable UUID id) {
        log.info("Recibida petición GET para obtener producto: {}", id);
        return productService.findById(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        log.info("Recibida petición PUT para actualizar producto: {}", id);
        return productService.update(id, product.getName(), product.getPriceBuy(), product.getPriceSell(), product.getCategoryId());
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable UUID id) {
        log.info("Recibida petición DELETE para borrar producto: {}", id);
        productService.delete(id);
    }
}