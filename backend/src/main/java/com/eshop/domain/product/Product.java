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
    private String nameNormalized;
    private BigDecimal priceBuy;
    private BigDecimal priceSell;
    private UUID categoryId;

    public Product(String name, BigDecimal priceBuy, BigDecimal priceSell, UUID categoryId) {
        validatePrices(priceBuy, priceSell);
        this.name = name;
        this.nameNormalized = normalize(name);
        this.priceBuy = priceBuy;
        this.priceSell = priceSell;
        this.categoryId = categoryId;
        this.setDeleted(false);
    }

    public void updateDetails(String name, BigDecimal priceBuy, BigDecimal priceSell, UUID categoryId) {
        validatePrices(priceBuy, priceSell);
        this.name = name;
        this.nameNormalized = normalize(name);
        this.priceBuy = priceBuy;
        this.priceSell = priceSell;
        this.categoryId = categoryId;
    }

    public void assignToCategory(UUID newCategoryId) {
        this.categoryId = newCategoryId;
    }

    private String normalize(String value) {
        if (value == null) return null;
        String normalized = java.text.Normalizer.normalize(value, java.text.Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

    private void validatePrices(BigDecimal priceBuy, BigDecimal priceSell) {
        if (priceBuy == null) {
            throw new IllegalArgumentException("Price buy cannot be null");
        }
        if (priceSell == null) {
            throw new IllegalArgumentException("Price sell cannot be null");
        }
        if (priceBuy.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price buy cannot be negative");
        }
        if (priceSell.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price sell cannot be negative");
        }
    }
}
