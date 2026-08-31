package com.eshop.infrastructure.persistence.product;

import com.eshop.domain.product.Product;
import com.eshop.infrastructure.persistence.base.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<Product, ProductEntity> {
}
