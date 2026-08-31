
package com.eshop.domain.product;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product create(CreateProductCommand command) {
        log.info("Creando nuevo producto: {}", command.name());
        Product product = new Product(command.name(), command.priceBuy(), command.priceSell(), command.categoryId());
        return productRepository.save(product);
    }

    public Page<Product> findAll(Pageable pageable) {
        log.info("Obteniendo todos los productos paginados");
        return productRepository.findAll(pageable);
    }

    public Product findById(UUID id) {
        log.info("Buscando producto con ID: {}", id);
        return productRepository.getOrThrow(id);
    }



    public Product update(UpdateProductCommand command) {
        log.info("Actualizando (parcialmente) producto con ID: {}", command.id());
        Product product = productRepository.getOrThrow(command.id());
        
        String finalName = Optional.ofNullable(command.name()).orElse(product.getName());
        BigDecimal finalPriceBuy = Optional.ofNullable(command.priceBuy()).orElse(product.getPriceBuy());
        BigDecimal finalPriceSell = Optional.ofNullable(command.priceSell()).orElse(product.getPriceSell());
        UUID finalCategoryId = Optional.ofNullable(command.categoryId()).orElse(product.getCategoryId());
        
        product.updateDetails(finalName, finalPriceBuy, finalPriceSell, finalCategoryId);
        return productRepository.save(product);
    }

    public void delete(UUID id) {
        log.info("Borrando producto con ID: {}", id);
        Product product = productRepository.getOrThrow(id);
        productRepository.delete(product);
    }
}
