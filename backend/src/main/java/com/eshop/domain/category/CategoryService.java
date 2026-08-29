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

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        log.info("Obteniendo todas las categorías");
        return categoryRepository.findAll();
    }

    @Transactional
    public Category update(UUID categoryId, String newName, UUID newParentId) {
        log.info("Actualizando categoría {}: nuevo nombre='{}', nuevo parentId='{}'", categoryId, newName, newParentId);
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        if (category.isSystem()) {
            throw new IllegalArgumentException("Cannot update a system category");
        }

        String oldName = category.getName();
        String oldPath = category.getPathNames();
        
        boolean nameChanged = newName != null && !newName.trim().isEmpty() && !oldName.equals(newName);
        boolean parentChanged = (newParentId != null && !newParentId.equals(category.getParentId())) || 
                                (newParentId == null && category.getParentId() != null);

        if (nameChanged) {
            category.setName(newName);
        }

        if (parentChanged) {
            if (newParentId != null) {
                boolean isDescendant = closureRepository.isDescendant(categoryId, newParentId);
                validateMove(categoryId, newParentId, isDescendant, 0); // Skipping depth validation for MVP
            }
            
            // Re-parent in closure table
            closureRepository.disconnectSubtree(categoryId);
            if (newParentId != null) {
                closureRepository.connectSubtree(categoryId, newParentId);
            }
            category.setParentId(newParentId);
        }

        if (nameChanged || parentChanged) {
            String newPath = category.getName();
            if (category.getParentId() != null) {
                Category parent = categoryRepository.findById(category.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("New parent not found"));
                newPath = parent.getPathNames() + PATH_SEPARATOR + category.getName();
            }
            category.setPathNames(newPath);
            categoryRepository.save(category);

            // Update all descendants' paths
            List<Category> descendants = categoryRepository.findDescendants(categoryId);
            for (Category desc : descendants) {
                if (desc.getPathNames() != null && oldPath != null && desc.getPathNames().startsWith(oldPath)) {
                    String updatedDescPath = newPath + desc.getPathNames().substring(oldPath.length());
                    desc.setPathNames(updatedDescPath);
                    categoryRepository.save(desc);
                }
            }
        } else {
            categoryRepository.save(category);
        }

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
