package com.eshop.infrastructure.web;

import com.eshop.TestcontainersConfiguration;
import com.eshop.domain.category.Category;
import com.eshop.domain.category.CategoryService;
import com.eshop.domain.product.ProductService;
import com.eshop.infrastructure.web.product.CreateProductRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class ProductFilteringIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Category catElectronics;
    private Category catAccessories;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        
        // Clean up products to have a clean state for the 5 test products
        productService.findAll(null, org.springframework.data.domain.Pageable.unpaged())
                      .forEach(p -> productService.delete(p.getId()));

        catElectronics = categoryService.create(new com.eshop.domain.category.CreateCategoryCommand("Electronics Test", null));
        catAccessories = categoryService.create(new com.eshop.domain.category.CreateCategoryCommand("Accessories Test", null));

        createProduct("Smartphone", 500, 800, catElectronics);
        createProduct("Árbol Mágico", 10, 20, catAccessories); // Accents for normalization test
        createProduct("Tablet", 200, 300, catElectronics);
        createProduct("Laptop", 800, 1200, catElectronics);
        createProduct("Monitor", 150, 250, catElectronics);
    }

    private void createProduct(String name, double buy, double sell, Category category) throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                name,
                BigDecimal.valueOf(buy),
                BigDecimal.valueOf(sell),
                category.getId()
        );
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldFilterByMinPrice() throws Exception {
        // minPrice = 500 -> should return Smartphone (800) and Laptop (1200)
        mockMvc.perform(get("/api/products?minPrice=500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[*].name", containsInAnyOrder("Smartphone", "Laptop")));
    }

    @Test
    void shouldFilterByMaxPrice() throws Exception {
        // maxPrice = 250 -> should return Árbol Mágico (20), Monitor (250)
        mockMvc.perform(get("/api/products?maxPrice=250"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.content[*].name", containsInAnyOrder("Árbol Mágico", "Monitor")));
    }

    @Test
    void shouldFilterByMinAndMaxPrice() throws Exception {
        // 250 <= price <= 800 -> Monitor (250), Tablet (300), Smartphone (800)
        mockMvc.perform(get("/api/products?minPrice=250&maxPrice=800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(3)))
                .andExpect(jsonPath("$.data.content[*].name", containsInAnyOrder("Monitor", "Tablet", "Smartphone")));
    }

    @Test
    void shouldFilterByCategory() throws Exception {
        // Accessories category -> Árbol Mágico
        mockMvc.perform(get("/api/products?categoryId=" + catAccessories.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].name", is("Árbol Mágico")));
    }

    @Test
    void shouldFilterByNameNormalized() throws Exception {
        // Querying for "arbol" should match "Árbol Mágico" thanks to normalization
        mockMvc.perform(get("/api/products?name=arbol"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].name", is("Árbol Mágico")));
                
        // Querying for "MÁGICO" should also work
        mockMvc.perform(get("/api/products?name=MÁGICO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].name", is("Árbol Mágico")));
    }
}
