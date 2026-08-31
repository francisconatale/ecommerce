package com.eshop.domain.category;
import com.eshop.infrastructure.persistence.category.SpringDataCategoryClosureRepository;
import org.springframework.transaction.annotation.Transactional;

import com.eshop.domain.product.Product;
import com.eshop.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.Objects;
import java.util.List;
import com.eshop.domain.exception.BusinessException;
import com.eshop.domain.exception.ResourceNotFoundException;
import com.eshop.domain.exception.CircularDependencyException;
import com.eshop.domain.exception.MaxDepthExceededException;
import com.eshop.domain.exception.SystemCategoryImmutableException;

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
        log.info("Creando nueva categorÃƒÂ­a: {} con parentId: {}", name, parentId);
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName(name);
        category.setParentId(parentId);
        category.setSystem(false);

        boolean hasParent = parentId != null;
        String pathNames = name;
        if (hasParent) {
            Category parent = getCategoryOrThrow(parentId);
            pathNames = parent.getPathNames() + PATH_SEPARATOR + name;
        }

        category.setPathNames(pathNames);
        categoryRepository.save(category);
        closureRepository.insertNodeIntoTree(category.getId(), hasParent ? parentId : category.getId());
        
        log.info("CategorÃƒÂ­a creada exitosamente con ID: {}", category.getId());
        return category;
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        log.info("Obteniendo todas las categorÃƒÂ­as");
        return categoryRepository.findAll();
    }

    @Transactional
    public Category update(UUID categoryId, String newName, UUID newParentId) {
        log.info("Actualizando categorÃƒÂ­a {}: nuevo nombre='{}', nuevo parentId='{}'", categoryId, newName, newParentId);
        Category category = getCategoryOrThrow(categoryId);
        validateNotSystemCategory(category);

        String oldPath = category.getPathNames();
        boolean nameChanged = updateNameIfChanged(category, newName);
        boolean parentChanged = updateParentIfChanged(category, newParentId);

        if (nameChanged || parentChanged) {
            updatePathsForCategoryAndDescendants(category, oldPath);
        }

        return categoryRepository.save(category);
    }

    private Category getCategoryOrThrow(UUID categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }

    private void validateNotSystemCategory(Category category) {
        if (category.isSystem()) {
            throw new SystemCategoryImmutableException("Cannot update a system category");
        }
    }

    private boolean updateNameIfChanged(Category category, String newName) {
        if (!category.getName().equals(newName)) {
            category.setName(newName);
            return true;
        }
        return false;
    }

    private boolean updateParentIfChanged(Category category, UUID newParentId) {
        if (Objects.equals(category.getParentId(), newParentId)) {
            return false;
        }

        if (newParentId != null) {
            boolean isDescendant = closureRepository.isDescendant(category.getId(), newParentId);
            validateMove(category.getId(), newParentId, isDescendant, 0);
        }
        
        closureRepository.disconnectSubtree(category.getId());
        if (newParentId != null) {
            closureRepository.connectSubtree(category.getId(), newParentId);
        }
        category.setParentId(newParentId);
        
        return true;
    }

    private void updatePathsForCategoryAndDescendants(Category category, String oldPath) {
        String newPath = calculateNewPath(category);
        category.setPathNames(newPath);
        
        List<Category> descendants = categoryRepository.findDescendants(category.getId());
        for (Category desc : descendants) {
            if (desc.getPathNames() != null && oldPath != null && desc.getPathNames().startsWith(oldPath)) {
                String updatedDescPath = newPath + desc.getPathNames().substring(oldPath.length());
                desc.setPathNames(updatedDescPath);
                categoryRepository.save(desc);
            }
        }
    }

    private String calculateNewPath(Category category) {
        if (category.getParentId() == null) {
            return category.getName();
        }
        Category parent = getCategoryOrThrow(category.getParentId());
        return parent.getPathNames() + PATH_SEPARATOR + category.getName();
    }

    @Transactional
    public void assignProduct(UUID productId, UUID categoryId) {
        log.info("Asignando producto {} a categorÃƒÂ­a {}", productId, categoryId);
        Category category = getCategoryOrThrow(categoryId);
        Product product = getProductOrThrow(productId);
        
        product.setCategoryId(category.getId());
        productRepository.save(product);
        log.info("Producto {} asignado exitosamente a categorÃƒÂ­a {}", productId, categoryId);
    }

    private Product getProductOrThrow(UUID productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
    }

    public void validateMove(UUID nodeId, UUID newParentId, boolean isNewParentDescendant, int projectedDepth) {
        if (isNewParentDescendant) {
            log.warn("Movimiento invÃƒÂ¡lido: ciclo detectado para nodeId {}", nodeId);
            throw new CircularDependencyException("Invalid move: cycle detected. Cannot move a category to its own descendant.");
        }
        
        if (projectedDepth > MAX_DEPTH) {
            log.warn("Movimiento invÃƒÂ¡lido: profundidad proyectada {} excede MAX_DEPTH", projectedDepth);
            throw new MaxDepthExceededException(MAX_DEPTH);
        }
    }
    
    public UUID determineNewParentForChildren(UUID deletedCategoryParentId) {
        return deletedCategoryParentId;
    }

    @Transactional
    public void delete(UUID categoryId) {
        log.info("Iniciando borrado de categorÃƒÂ­a {}", categoryId);
        Category category = getCategoryOrThrow(categoryId);
        validateNotSystemCategory(category);

        UUID parentId = determineNewParentForChildren(category.getParentId());
        
        log.info("Reasignando hijos y productos de la categorÃƒÂ­a {} a su padre {}", categoryId, parentId);
        reparentChildrenAndUpdatePaths(categoryId, parentId, category.getName());
        reparentProducts(categoryId, parentId);
        updateClosureTree(categoryId);
        
        categoryRepository.delete(category);
        log.info("CategorÃƒÂ­a {} borrada exitosamente (soft delete)", categoryId);
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

