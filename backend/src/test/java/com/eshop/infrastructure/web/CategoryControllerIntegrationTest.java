package com.eshop.infrastructure.web;

import com.eshop.TestcontainersConfiguration;
import com.eshop.domain.category.Category;
import com.eshop.domain.category.CategoryService;
import com.eshop.infrastructure.web.category.CreateCategoryRequest;
import com.eshop.infrastructure.web.category.UpdateCategoryRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.UUID;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class CategoryControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Autowired
    private CategoryService categoryService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldCreateCategory() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("Initial Name", null);
        
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Initial Name"));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        Category created = categoryService.create(new com.eshop.domain.category.CreateCategoryCommand("Initial Name", null));
        
        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest("Updated Name", null);

        mockMvc.perform(patch("/api/categories/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Name"));
                
        Category updated = categoryService.findAll(org.springframework.data.domain.Pageable.unpaged()).stream().filter(c -> c.getId().equals(created.getId())).findFirst().get();
        assertThat(updated.getName()).isEqualTo("Updated Name");
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        Category parent = categoryService.create(new com.eshop.domain.category.CreateCategoryCommand("Parent", null));
        Category child = categoryService.create(new com.eshop.domain.category.CreateCategoryCommand("Child", parent.getId()));
        
        mockMvc.perform(delete("/api/categories/" + child.getId()))
                .andExpect(status().isNoContent());
                
        boolean exists = categoryService.findAll(org.springframework.data.domain.Pageable.unpaged()).stream().anyMatch(c -> c.getId().equals(child.getId()));
        assertThat(exists).isFalse();
    }
}
