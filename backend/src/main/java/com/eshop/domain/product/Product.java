package com.eshop.domain.product;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private UUID id;
    private String name;
    private BigDecimal priceBuy;
    private BigDecimal priceSell;
    private UUID categoryId;
}
