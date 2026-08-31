package C:.Users.franc.OneDrive.Desktop.prototype-eshop.backend.src.test.java.com.eshop.application;

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

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class CategoryServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    void shouldUpdateParentAndRecalculatePathsForDescendants() {
        Category root1 = categoryService.create("Root 1", null);
        Category root2 = categoryService.create("Root 2", null);
        Category child = categoryService.create("Child", root1.getId());
        Category grandChild = categoryService.create("GrandChild", child.getId());

        assertThat(child.getPathNames()).isEqualTo("Root 1 > Child");
        assertThat(grandChild.getPathNames()).isEqualTo("Root 1 > Child > GrandChild");

        // Act: Move child to root2
        categoryService.update(child.getId(), "Child Renamed", root2.getId());

        // Assert
        Category updatedChild = categoryService.findAll().stream().filter(c -> c.getId().equals(child.getId())).findFirst().get();
        Category updatedGrandChild = categoryService.findAll().stream().filter(c -> c.getId().equals(grandChild.getId())).findFirst().get();

        assertThat(updatedChild.getPathNames()).isEqualTo("Root 2 > Child Renamed");
        assertThat(updatedGrandChild.getPathNames()).isEqualTo("Root 2 > Child Renamed > GrandChild");
    }

    @Test
    @Transactional
    void shouldFetchProductsFromRootAndAllDescendants() {
        // Arrange
        Category electronics = categoryService.create("Electronics", null);
        Category phones = categoryService.create("Phones", electronics.getId());
        Category laptops = categoryService.create("Laptops", electronics.getId());
        Category gamingLaptops = categoryService.create("Gaming Laptops", laptops.getId());

        productRepository.save(new Product(UUID.randomUUID(), "Generic Wire", BigDecimal.TEN, BigDecimal.TEN, electronics.getId(), false));
        productRepository.save(new Product(UUID.randomUUID(), "iPhone", BigDecimal.TEN, BigDecimal.TEN, phones.getId(), false));
        productRepository.save(new Product(UUID.randomUUID(), "MacBook", BigDecimal.TEN, BigDecimal.TEN, laptops.getId(), false));
        productRepository.save(new Product(UUID.randomUUID(), "Alienware", BigDecimal.TEN, BigDecimal.TEN, gamingLaptops.getId(), false));

        // Act
        List<Product> electronicsProducts = productRepository.findByCategoryAndDescendants(electronics.getId());
        
        // Assert: It should bring ALL 4 products since they all belong to electronics or its descendants
        assertThat(electronicsProducts).hasSize(4);
        assertThat(electronicsProducts).extracting(Product::getName)
                .containsExactlyInAnyOrder("Generic Wire", "iPhone", "MacBook", "Alienware");
    }

    @Test
    @Transactional
    void shouldDistinguishProductsBetweenSiblings() {
        // Arrange
        Category electronics = categoryService.create("Electronics", null);
        Category phones = categoryService.create("Phones", electronics.getId());
        Category laptops = categoryService.create("Laptops", electronics.getId());

        productRepository.save(new Product(UUID.randomUUID(), "iPhone", BigDecimal.TEN, BigDecimal.TEN, phones.getId(), false));
        productRepository.save(new Product(UUID.randomUUID(), "MacBook", BigDecimal.TEN, BigDecimal.TEN, laptops.getId(), false));

        // Act
        List<Product> phonesProducts = productRepository.findByCategoryAndDescendants(phones.getId());
        List<Product> laptopsProducts = productRepository.findByCategoryAndDescendants(laptops.getId());
        
        // Assert
        assertThat(phonesProducts).hasSize(1);
        assertThat(phonesProducts.get(0).getName()).isEqualTo("iPhone");

        assertThat(laptopsProducts).hasSize(1);
        assertThat(laptopsProducts.get(0).getName()).isEqualTo("MacBook");
    }
}