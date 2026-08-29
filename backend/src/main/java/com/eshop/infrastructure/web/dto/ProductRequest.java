package com.eshop.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest(String name, BigDecimal priceBuy, BigDecimal priceSell, UUID categoryId) {}