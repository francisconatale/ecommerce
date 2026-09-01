package com.eshop.infrastructure.persistence.product;

import com.eshop.domain.product.ProductFilter;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecifications {

    public static Specification<ProductEntity> withFilter(ProductFilter filter) {
        Specification<ProductEntity> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (filter == null) {
            return spec;
        }

        if (filter.getName() != null && !filter.getName().trim().isEmpty()) {
            String normalizedFilter = java.text.Normalizer.normalize(filter.getName(), java.text.Normalizer.Form.NFD)
                    .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            spec = spec.and((root, query, cb) -> 
                cb.like(root.get("nameNormalized"), "%" + normalizedFilter + "%"));
        }
        if (filter.getCategoryId() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.equal(root.get("categoryId"), filter.getCategoryId()));
        }
        if (filter.getMinPrice() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.greaterThanOrEqualTo(root.get("priceSell"), filter.getMinPrice()));
        }
        if (filter.getMaxPrice() != null) {
            spec = spec.and((root, query, cb) -> 
                cb.lessThanOrEqualTo(root.get("priceSell"), filter.getMaxPrice()));
        }

        return spec;
    }
}
