package com.eshop.application;

import com.eshop.TestcontainersConfiguration;
import com.eshop.domain.category.Category;
import com.eshop.domain.category.CategoryService;
import com.eshop.domain.product.Product;
import com.eshop.domain.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class CategorizeProductUseCaseTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    void shouldAssignProductToLeafCategory() {
        // Arrange
        Category root = categoryService.create("Electronics", null);
        Category leaf = categoryService.create("Smartphones", root.getId());
        
        Product product = new Product(UUID.randomUUID(), "iPhone 15", BigDecimal.valueOf(800), BigDecimal.valueOf(1000), root.getId(), false);
        product = productRepository.save(product);

        // Act
        categoryService.assignProduct(product.getId(), leaf.getId());

        // Assert
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getCategoryId()).isEqualTo(leaf.getId());
    }
}
