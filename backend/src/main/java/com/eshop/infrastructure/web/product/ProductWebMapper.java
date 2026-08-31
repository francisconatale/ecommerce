package com.eshop.infrastructure.web.product;

import com.eshop.domain.product.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductWebMapper {
    ProductResponse toResponse(Product product);
}
