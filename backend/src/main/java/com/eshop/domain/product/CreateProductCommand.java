package com.eshop.domain.product;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductCommand(String name, BigDecimal priceBuy, BigDecimal priceSell, UUID categoryId) {
}
