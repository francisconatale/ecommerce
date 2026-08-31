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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    public Category create(CreateCategoryCommand command) {
        log.info("Creando nueva categoría: {} con parentId: {}", command.name(), command.parentId());
        Category category = new Category(command.name(), command.parentId());

        Category parent = category.isRoot() ? null : categoryRepository.getOrThrow(command.parentId());
        category.recalculatePath(parent);

        category = categoryRepository.save(category);
        closureRepository.insertNodeIntoTree(category.getId(), category.isRoot() ? category.getId() : command.parentId());
        
        log.info("Categoría creada exitosamente con ID: {}", category.getId());
        return category;
    }

    @Transactional(readOnly = true)
    public Page<Category> findAll(Pageable pageable) {
        log.info("Obteniendo todas las categorías paginadas");
        return categoryRepository.findAll(pageable);
    }

    @Transactional
    public Category update(UpdateCategoryCommand command) {
        log.info("Actualizando (parcialmente) categoría {}: nuevo nombre='{}', nuevo parentId='{}'", command.categoryId(), command.newName(), command.newParentId());
        Category category = categoryRepository.getOrThrow(command.categoryId());
        category.validateNotSystem();

        String finalName = java.util.Optional.ofNullable(command.newName()).orElse(category.getName());
        UUID finalParentId = java.util.Optional.ofNullable(command.newParentId()).orElse(category.getParentId());

        String oldPath = category.getPathNames();
        boolean nameChanged = category.rename(finalName);
        boolean parentChanged = handleParentChange(category, finalParentId);

        if (nameChanged || parentChanged) {
            updatePathsForCategoryAndDescendants(category, oldPath);
        }

        return categoryRepository.save(category);
    }

    private boolean handleParentChange(Category category, UUID newParentId) {
        if (Objects.equals(category.getParentId(), newParentId)) {
            return false;
        }

        boolean isMovingToRoot = (newParentId == null);

        if (!isMovingToRoot) {
            boolean isDescendant = closureRepository.isDescendant(category.getId(), newParentId);
            validateMove(category.getId(), newParentId, isDescendant, 0);
        }
        
        closureRepository.disconnectSubtree(category.getId());

        if (!isMovingToRoot) {
            closureRepository.connectSubtree(category.getId(), newParentId);
        }
        return category.moveToParent(newParentId);
    }

    private void updatePathsForCategoryAndDescendants(Category category, String oldPath) {
        Category parent = category.isRoot() ? null : categoryRepository.getOrThrow(category.getParentId());
        category.recalculatePath(parent);
        
        List<Category> descendants = categoryRepository.findDescendants(category.getId());
        for (Category desc : descendants) {
            desc.replacePathPrefix(oldPath, category.getPathNames());
            categoryRepository.save(desc);
        }
    }

    @Transactional
    public void assignProduct(UUID productId, UUID categoryId) {
        log.info("Asignando producto {} a categoría {}", productId, categoryId);
        Category category = categoryRepository.getOrThrow(categoryId);
        Product product = productRepository.getOrThrow(productId);
        
        product.assignToCategory(category.getId());
        productRepository.save(product);
        log.info("Producto {} asignado exitosamente a categoría {}", productId, categoryId);
    }

    public void validateMove(UUID nodeId, UUID newParentId, boolean isNewParentDescendant, int projectedDepth) {
        if (isNewParentDescendant) {
            log.warn("Movimiento invalido: ciclo detectado para nodeId {}", nodeId);
            throw new CircularDependencyException("Invalid move: cycle detected. Cannot move a category to its own descendant.");
        }
        
        if (projectedDepth > MAX_DEPTH) {
            log.warn("Movimiento invalido: profundidad proyectada {} excede MAX_DEPTH", projectedDepth);
            throw new MaxDepthExceededException(MAX_DEPTH);
        }
    }
    
    public UUID determineNewParentForChildren(UUID deletedCategoryParentId) {
        return deletedCategoryParentId;
    }

    @Transactional
    public void delete(UUID categoryId) {
        log.info("Iniciando borrado de categoría {}", categoryId);
        Category category = categoryRepository.getOrThrow(categoryId);
        category.validateNotSystem();

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
        
        for (Category desc : descendants) {
            if (categoryId.equals(desc.getParentId())) {
                desc.moveToParent(newParentId);
            }
            desc.removeDeletedParentFromPath(deletedCategoryName);
            categoryRepository.save(desc);
        }
    }

    private void reparentProducts(UUID categoryId, UUID newParentId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        for (Product product : products) {
            product.assignToCategory(newParentId);
            productRepository.save(product);
        }
    }

    private void updateClosureTree(UUID categoryId) {
        closureRepository.decreaseDepthForPathsThroughNode(categoryId);
        closureRepository.removeNodeFromTree(categoryId);
    }
}

