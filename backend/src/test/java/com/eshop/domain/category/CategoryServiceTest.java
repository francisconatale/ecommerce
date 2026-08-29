package com.eshop.domain.category;

import com.eshop.TestcontainersConfiguration;
import com.eshop.domain.product.Product;
import com.eshop.domain.product.ProductRepository;
import com.eshop.infrastructure.persistence.SpringDataCategoryClosureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Transactional
public class CategoryServiceTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService service;

    @Test
    void shouldDeleteRootCategory() {
        // Deleting "Root"
        Category root = service.create("Root", null);
        Category child = service.create("Child", root.getId());

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Prod");
        product.setPriceBuy(java.math.BigDecimal.ZERO);
        product.setPriceSell(java.math.BigDecimal.ZERO);
        product.setCategoryId(root.getId());
        productRepository.save(product);

        service.delete(root.getId());

        // Child should now be at root (parentId null) and path updated
        Category updatedChild = categoryRepository.findById(child.getId()).orElseThrow();
        assertNull(updatedChild.getParentId());
        assertEquals("Child", updatedChild.getPathNames());

        // Product should be moved to root (null)
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertNull(updatedProduct.getCategoryId());
    }

    @Test
    void shouldDeleteIntermediateCategory() {
        // Deleting "Middle" (Parent: "Root", Child: "Leaf")
        Category root = service.create("Root", null);
        Category middle = service.create("Middle", root.getId());
        Category leaf = service.create("Leaf", middle.getId());

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Prod");
        product.setPriceBuy(java.math.BigDecimal.ZERO);
        product.setPriceSell(java.math.BigDecimal.ZERO);
        product.setCategoryId(middle.getId());
        productRepository.save(product);

        service.delete(middle.getId());

        // Leaf should point to Root and its path updated
        Category updatedLeaf = categoryRepository.findById(leaf.getId()).orElseThrow();
        assertEquals(root.getId(), updatedLeaf.getParentId());
        assertEquals("Root > Leaf", updatedLeaf.getPathNames());

        // Product should be moved to Root
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(root.getId(), updatedProduct.getCategoryId());
    }

    @Test
    void shouldDeleteLeafCategory() {
        Category root = service.create("Root", null);
        Category middle = service.create("Middle", root.getId());
        Category leaf = service.create("Leaf", middle.getId());

        Product product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Prod");
        product.setPriceBuy(java.math.BigDecimal.ZERO);
        product.setPriceSell(java.math.BigDecimal.ZERO);
        product.setCategoryId(leaf.getId());
        productRepository.save(product);

        service.delete(leaf.getId());

        // Product should be moved to Middle
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(middle.getId(), updatedProduct.getCategoryId());
        
        assertTrue(categoryRepository.findById(leaf.getId()).isEmpty());
    }

    @Test
    void shouldUpdatePathsForMultipleDescendants() {
        Category parent = service.create("Parent", null);
        Category child1 = service.create("Child1", parent.getId());
        Category child2 = service.create("Child2", parent.getId());
        Category grandChild = service.create("GrandChild", child1.getId());

        service.delete(parent.getId());

        Category uChild1 = categoryRepository.findById(child1.getId()).orElseThrow();
        Category uChild2 = categoryRepository.findById(child2.getId()).orElseThrow();
        Category uGrandChild = categoryRepository.findById(grandChild.getId()).orElseThrow();

        assertEquals("Child1", uChild1.getPathNames());
        assertEquals("Child2", uChild2.getPathNames());
        assertEquals("Child1 > GrandChild", uGrandChild.getPathNames());
    }

    @Test
    void shouldReassignProductsToGrandparent() {
        Category grandparent = service.create("Grandparent", null);
        Category parent = service.create("Parent", grandparent.getId());

        Product p1 = new Product(); p1.setId(UUID.randomUUID()); p1.setName("p1"); p1.setPriceBuy(java.math.BigDecimal.ZERO); p1.setPriceSell(java.math.BigDecimal.ZERO); p1.setCategoryId(parent.getId());
        Product p2 = new Product(); p2.setId(UUID.randomUUID()); p2.setName("p2"); p2.setPriceBuy(java.math.BigDecimal.ZERO); p2.setPriceSell(java.math.BigDecimal.ZERO); p2.setCategoryId(parent.getId());
        productRepository.save(p1);
        productRepository.save(p2);

        service.delete(parent.getId());

        Product up1 = productRepository.findById(p1.getId()).orElseThrow();
        Product up2 = productRepository.findById(p2.getId()).orElseThrow();

        assertEquals(grandparent.getId(), up1.getCategoryId());
        assertEquals(grandparent.getId(), up2.getCategoryId());
    }
}
