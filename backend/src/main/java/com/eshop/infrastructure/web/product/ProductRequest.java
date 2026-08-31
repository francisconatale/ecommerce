package com.eshop.infrastructure.web.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(String name, BigDecimal priceBuy, BigDecimal priceSell, UUID categoryId) {}