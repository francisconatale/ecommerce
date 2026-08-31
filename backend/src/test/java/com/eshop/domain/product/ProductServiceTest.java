package com.eshop.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import com.eshop.domain.product.CreateProductCommand;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProduct() {
        CreateProductCommand command = new CreateProductCommand("Laptop", BigDecimal.valueOf(1000), BigDecimal.valueOf(1500), UUID.randomUUID());
        
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        Product result = productService.create(command);

        assertThat(result.getName()).isEqualTo("Laptop");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldDeleteProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product("Test", BigDecimal.TEN, BigDecimal.valueOf(20), UUID.randomUUID());
        product.setId(id);
        when(productRepository.getOrThrow(id)).thenReturn(product);

        productService.delete(id);

        verify(productRepository).delete(product);
    }
}