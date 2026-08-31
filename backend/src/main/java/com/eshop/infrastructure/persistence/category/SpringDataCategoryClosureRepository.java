package com.eshop.infrastructure.persistence.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SpringDataCategoryClosureRepository extends JpaRepository<CategoryClosureEntity, CategoryClosureEntity.CategoryClosureId> {

    @Modifying
    @Query(value = "INSERT INTO category_closure (ancestor_id, descendant_id, depth) " +
                   "SELECT ancestor_id, :descendantId, depth + 1 " +
                   "FROM category_closure WHERE descendant_id = :parentId " +
                   "UNION ALL " +
                   "SELECT :descendantId, :descendantId, 0", 
           nativeQuery = true)
    void insertNodeIntoTree(@Param("descendantId") UUID descendantId, @Param("parentId") UUID parentId);

    @Modifying
    @Query(value = "UPDATE category_closure SET depth = depth - 1 " +
                   "WHERE ancestor_id IN (SELECT a.ancestor_id FROM (SELECT ancestor_id FROM category_closure WHERE descendant_id = :nodeId AND ancestor_id != :nodeId) a) " +
                   "AND descendant_id IN (SELECT d.descendant_id FROM (SELECT descendant_id FROM category_closure WHERE ancestor_id = :nodeId AND descendant_id != :nodeId) d)", nativeQuery = true)
    void decreaseDepthForPathsThroughNode(@Param("nodeId") UUID nodeId);

    @Modifying
    @Query(value = "DELETE FROM category_closure WHERE descendant_id = :nodeId OR ancestor_id = :nodeId", nativeQuery = true)
    void removeNodeFromTree(@Param("nodeId") UUID nodeId);

    @Modifying
    @Query(value = "DELETE FROM category_closure " +
                   "WHERE descendant_id IN (SELECT descendant_id FROM (SELECT descendant_id FROM category_closure WHERE ancestor_id = :nodeId) AS sub1) " +
                   "AND ancestor_id IN (SELECT ancestor_id FROM (SELECT ancestor_id FROM category_closure WHERE descendant_id = :nodeId AND ancestor_id != :nodeId) AS sub2)", nativeQuery = true)
    void disconnectSubtree(@Param("nodeId") UUID nodeId);

    @Modifying
    @Query(value = "INSERT INTO category_closure (ancestor_id, descendant_id, depth) " +
                   "SELECT supertree.ancestor_id, subtree.descendant_id, supertree.depth + subtree.depth + 1 " +
                   "FROM category_closure supertree CROSS JOIN category_closure subtree " +
                   "WHERE supertree.descendant_id = :newParentId AND subtree.ancestor_id = :nodeId", nativeQuery = true)
    void connectSubtree(@Param("nodeId") UUID nodeId, @Param("newParentId") UUID newParentId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM category_closure WHERE ancestor_id = :nodeId AND descendant_id = :newParentId", nativeQuery = true)
    boolean isDescendant(@Param("nodeId") UUID nodeId, @Param("newParentId") UUID newParentId);
}
