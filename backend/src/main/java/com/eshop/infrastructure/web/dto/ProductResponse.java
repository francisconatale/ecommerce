package com.eshop.infrastructure.web.dto;

import com.eshop.domain.product.Product;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(UUID id, String name, BigDecimal priceBuy, BigDecimal priceSell, UUID categoryId) {
    public static ProductResponse fromDomain(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getPriceBuy(),
            product.getPriceSell(),
            product.getCategoryId()
        );
    }
}