package com.eshop.infrastructure.persistence.category;

import com.eshop.domain.category.Category;
import com.eshop.infrastructure.persistence.base.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<Category, CategoryEntity> {
}
