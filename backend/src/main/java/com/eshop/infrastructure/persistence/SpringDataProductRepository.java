package com.eshop.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, UUID> {
    
    List<ProductEntity> findByCategoryId(UUID categoryId);
    
    interface ProductWithBreadcrumb {
        UUID getId();
        String getName();
        Double getPriceSell();
        String getBreadcrumb(); // Maps to category.path_names
    }

    /**
     * T014: Fetch all products and their breadcrumbs in O(1) for a given category tree.
     */
    @Query(value = "SELECT p.id as id, p.name as name, p.price_sell as priceSell, c.path_names as breadcrumb " +
                   "FROM product p " +
                   "JOIN category_closure cc ON p.category_id = cc.descendant_id " +
                   "JOIN category c ON p.category_id = c.id " +
                   "WHERE cc.ancestor_id = :categoryId " +
                   "AND p.deleted = false", 
           nativeQuery = true)
    List<ProductWithBreadcrumb> findProductsByCategoryDescendantsWithBreadcrumb(@Param("categoryId") UUID categoryId);
}
