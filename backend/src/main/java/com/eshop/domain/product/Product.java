package com.eshop.domain.product;

import com.eshop.domain.base.BaseEntity;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseEntity {
    private String name;
    private BigDecimal priceBuy;
    private BigDecimal priceSell;
    private UUID categoryId;
    
    public Product(UUID id, String name, BigDecimal priceBuy, BigDecimal priceSell, UUID categoryId, boolean deleted) {
        this.setDeleted(deleted);
        this.setId(id);
        this.name = name;
        this.priceBuy = priceBuy;
        this.priceSell = priceSell;
        this.categoryId = categoryId;
    }

    public Product(String name, BigDecimal priceBuy, BigDecimal priceSell, UUID categoryId) {
        this.name = name;
        this.priceBuy = priceBuy;
        this.priceSell = priceSell;
        this.categoryId = categoryId;
        this.setDeleted(false);
    }
}
