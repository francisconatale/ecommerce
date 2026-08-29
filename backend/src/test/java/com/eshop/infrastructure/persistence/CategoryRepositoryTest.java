package com.eshop.infrastructure.persistence;

import com.eshop.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private SpringDataCategoryRepository repository;

    @Test
    void shouldSaveCategoryAndGenerateId() {
        CategoryEntity category = new CategoryEntity();
        category.setId(UUID.randomUUID());
        category.setName("Electrónica");
        category.setSystem(false);
        category.setPathNames("Electrónica");
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category.setDeleted(false);

        CategoryEntity saved = repository.save(category);
        assertNotNull(saved.getId());
    }
}
