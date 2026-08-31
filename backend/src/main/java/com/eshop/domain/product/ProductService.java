package com.eshop.domain.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(String name, BigDecimal priceBuy, BigDecimal priceSell, UUID categoryId) {
        log.info("Creando nuevo producto: {}", name);
        Product product = new Product(name, priceBuy, priceSell, categoryId);
        return productRepository.save(product);
    }

    public List<Product> findAll() {
        log.info("Obteniendo todos los productos");
        return productRepository.findAll();
    }

    public Product findById(UUID id) {
        log.info("Buscando producto con ID: {}", id);
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public Product update(UUID id, String name, BigDecimal priceBuy, BigDecimal priceSell, UUID categoryId) {
        log.info("Actualizando producto con ID: {}", id);
        Product product = findById(id);
        product.setName(name);
        product.setPriceBuy(priceBuy);
        product.setPriceSell(priceSell);
        product.setCategoryId(categoryId);
        return productRepository.save(product);
    }

    public void delete(UUID id) {
        log.info("Borrando producto con ID: {}", id);
        Product product = findById(id);
        productRepository.delete(product);
    }
}