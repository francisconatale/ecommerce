package com.eshop.domain.product;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductWithNormalizedName() {
        UUID categoryId = UUID.randomUUID();
        Product product = new Product("Café Exprés", new BigDecimal("10.0"), new BigDecimal("15.0"), categoryId);

        assertEquals("Café Exprés", product.getName());
        assertEquals("cafe expres", product.getNameNormalized());
        assertEquals(new BigDecimal("10.0"), product.getPriceBuy());
        assertEquals(new BigDecimal("15.0"), product.getPriceSell());
        assertEquals(categoryId, product.getCategoryId());
        assertFalse(product.isDeleted());
    }

    @Test
    void shouldUpdateDetailsWithNormalizedName() {
        UUID categoryId = UUID.randomUUID();
        Product product = new Product("Initial Name", new BigDecimal("10.0"), new BigDecimal("15.0"), categoryId);

        UUID newCategoryId = UUID.randomUUID();
        product.updateDetails("Té Verde", new BigDecimal("12.0"), new BigDecimal("18.0"), newCategoryId);

        assertEquals("Té Verde", product.getName());
        assertEquals("te verde", product.getNameNormalized());
        assertEquals(new BigDecimal("12.0"), product.getPriceBuy());
        assertEquals(new BigDecimal("18.0"), product.getPriceSell());
        assertEquals(newCategoryId, product.getCategoryId());
    }

    @Test
    void shouldAssignToCategory() {
        UUID initialCategoryId = UUID.randomUUID();
        Product product = new Product("Test", new BigDecimal("10.0"), new BigDecimal("15.0"), initialCategoryId);

        UUID newCategoryId = UUID.randomUUID();
        product.assignToCategory(newCategoryId);

        assertEquals(newCategoryId, product.getCategoryId());
    }

    @Test
    void shouldThrowExceptionWhenPriceBuyIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product("Test", null, new BigDecimal("15.0"), UUID.randomUUID());
        });
        assertEquals("Price buy cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPriceSellIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product("Test", new BigDecimal("10.0"), null, UUID.randomUUID());
        });
        assertEquals("Price sell cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPriceBuyIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product("Test", new BigDecimal("-1.0"), new BigDecimal("15.0"), UUID.randomUUID());
        });
        assertEquals("Price buy cannot be negative", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPriceSellIsNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Product("Test", new BigDecimal("10.0"), new BigDecimal("-1.0"), UUID.randomUUID());
        });
        assertEquals("Price sell cannot be negative", exception.getMessage());
    }
}
