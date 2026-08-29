package com.eshop.domain.category;
import com.eshop.infrastructure.persistence.SpringDataCategoryClosureRepository;
import org.springframework.transaction.annotation.Transactional;

import com.eshop.domain.product.Product;
import com.eshop.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CategoryService {
    
    private static final int MAX_DEPTH = 10;
    private static final String PATH_SEPARATOR = " > ";

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SpringDataCategoryClosureRepository closureRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository, SpringDataCategoryClosureRepository closureRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.closureRepository = closureRepository;
    }

    @Transactional
    public Category create(String name, UUID parentId) {
        log.info("Creando nueva categoría: {} con parentId: {}", name, parentId);
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName(name);
        category.setParentId(parentId);
        category.setSystem(false);

        boolean hasParent = parentId != null;
        String pathNames = name;
        if (hasParent) {
            Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            pathNames = parent.getPathNames() + PATH_SEPARATOR + name;
        }

        category.setPathNames(pathNames);
        categoryRepository.save(category);
        closureRepository.insertNodeIntoTree(category.getId(), hasParent ? parentId : category.getId());
        
        log.info("Categoría creada exitosamente con ID: {}", category.getId());
        return category;
    }

    @Transactional
    public void assignProduct(UUID productId, UUID categoryId) {
        log.info("Asignando producto {} a categoría {}", productId, categoryId);
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        product.setCategoryId(categoryId);
        productRepository.save(product);
        log.info("Producto {} asignado exitosamente a categoría {}", productId, categoryId);
    }

    /**
     * Validates if a move operation is legal.
     * 
     * @param nodeId The category being moved
     * @param newParentId The target parent category
     * @param isNewParentDescendant True if the database indicates newParentId is a descendant of nodeId
     * @param projectedDepth The calculated depth of the deepest node in the moved subtree after the move
     */
    public void validateMove(UUID nodeId, UUID newParentId, boolean isNewParentDescendant, int projectedDepth) {
        if (isNewParentDescendant) {
            log.warn("Movimiento inválido: ciclo detectado para nodeId {}", nodeId);
            throw new IllegalArgumentException("Invalid move: cycle detected. Cannot move a category to its own descendant.");
        }
        
        if (projectedDepth > MAX_DEPTH) {
            log.warn("Movimiento inválido: profundidad proyectada {} excede MAX_DEPTH", projectedDepth);
            throw new IllegalArgumentException("Invalid move: exceeds maximum depth of " + MAX_DEPTH + " levels.");
        }
    }

    /**
     * Determines the new parent ID for children and products when a category is deleted.
     * According to our Domain rules, they inherit the grandparent (the deleted category's parent).
     * 
     * @param deletedCategoryParentId The parentId of the category being deleted
     * @return The new parentId for the orphans
     */
    public UUID determineNewParentForChildren(UUID deletedCategoryParentId) {
        return deletedCategoryParentId;
    }

    @Transactional
    public void delete(UUID categoryId) {
        log.info("Iniciando borrado de categoría {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));
            
        if (category.isSystem()) {
            log.error("Intento de borrar categoría del sistema: {}", categoryId);
            throw new IllegalArgumentException("Cannot delete a system category");
        }

        UUID parentId = determineNewParentForChildren(category.getParentId());
        
        log.info("Reasignando hijos y productos de la categoría {} a su padre {}", categoryId, parentId);
        reparentChildrenAndUpdatePaths(categoryId, parentId, category.getName());
        reparentProducts(categoryId, parentId);
        updateClosureTree(categoryId);
        
        categoryRepository.delete(category);
        log.info("Categoría {} borrada exitosamente (soft delete)", categoryId);
    }

    private void reparentChildrenAndUpdatePaths(UUID categoryId, UUID newParentId, String deletedCategoryName) {
        List<Category> descendants = categoryRepository.findDescendants(categoryId);
        String stringToRemove = deletedCategoryName + PATH_SEPARATOR;
        
        for (Category desc : descendants) {
            if (categoryId.equals(desc.getParentId())) {
                desc.setParentId(newParentId);
            }
            if (desc.getPathNames() != null) {
                desc.setPathNames(desc.getPathNames().replace(stringToRemove, ""));
            }
            categoryRepository.save(desc);
        }
    }

    private void reparentProducts(UUID categoryId, UUID newParentId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        for (Product product : products) {
            product.setCategoryId(newParentId);
            productRepository.save(product);
        }
    }

    private void updateClosureTree(UUID categoryId) {
        closureRepository.decreaseDepthForPathsThroughNode(categoryId);
        closureRepository.removeNodeFromTree(categoryId);
    }
}
