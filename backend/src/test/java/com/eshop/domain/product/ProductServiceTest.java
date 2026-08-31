package com.eshop.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void shouldCreateProduct() {
        Product savedProduct = new Product(UUID.randomUUID(), "Test", BigDecimal.TEN, BigDecimal.valueOf(20), UUID.randomUUID(), false);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.create("Test", BigDecimal.TEN, BigDecimal.valueOf(20), UUID.randomUUID());

        assertThat(result.getName()).isEqualTo("Test");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldDeleteProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product(id, "Test", BigDecimal.TEN, BigDecimal.valueOf(20), UUID.randomUUID(), false);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productService.delete(id);

        verify(productRepository).delete(product);
    }
}