package com.eshop.domain.product;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {
    private UUID id;
    private String name;
    private BigDecimal priceBuy;
    private BigDecimal priceSell;
    private UUID categoryId;
    
    
    public UUID getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public BigDecimal getPriceBuy() { return priceBuy; }
    public void setPriceBuy(BigDecimal priceBuy) { this.priceBuy = priceBuy; }
    
    public BigDecimal getPriceSell() { return priceSell; }
    public void setPriceSell(BigDecimal priceSell) { this.priceSell = priceSell; }
}
