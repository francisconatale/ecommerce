package com.eshop.infrastructure.persistence.category;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    
    @Query(value = "SELECT c.* FROM category c INNER JOIN category_closure cc ON c.id = cc.descendant_id WHERE cc.ancestor_id = :nodeId AND cc.descendant_id != :nodeId", nativeQuery = true)
    List<CategoryEntity> findDescendants(@Param("nodeId") UUID nodeId);
}
