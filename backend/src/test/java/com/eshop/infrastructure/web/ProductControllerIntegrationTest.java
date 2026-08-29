package com.eshop.infrastructure.web;

import com.eshop.TestcontainersConfiguration;
import com.eshop.domain.product.Product;
import com.eshop.domain.category.Category;
import com.eshop.domain.category.CategoryService;
import com.eshop.domain.product.ProductService;
import com.eshop.infrastructure.web.dto.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class ProductControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        Category category = categoryService.create("Product Category", null);
        
        ProductRequest request = new ProductRequest(
            "Integration Test Product",
            BigDecimal.valueOf(10.5),
            BigDecimal.valueOf(20.0),
            category.getId()
        );
        
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Integration Test Product"));
    }

    @Test
    void shouldSoftDeleteProduct() throws Exception {
        Category category = categoryService.create("Category for Deletion", null);
        Product product = productService.create("To Be Deleted Product", BigDecimal.ONE, BigDecimal.TEN, category.getId());
        
        mockMvc.perform(delete("/api/products/" + product.getId()))
                .andExpect(status().isNoContent());
                
        // Verification: It shouldn't be found in findAll
        boolean exists = productService.findAll().stream().anyMatch(p -> p.getId().equals(product.getId()));
        assertThat(exists).isFalse();
    }
}