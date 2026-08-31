package com.eshop.domain.category;

import com.eshop.domain.base.BaseEntity;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Category extends BaseEntity {
    private String name;
    private UUID parentId;
    private boolean isSystem;
    private String pathNames;
    
    public Category(UUID id, String name, UUID parentId, boolean isSystem, String pathNames, boolean deleted) {
        this.setId(id);
        this.name = name;
        this.parentId = parentId;
        this.isSystem = isSystem;
        this.pathNames = pathNames;
        this.setDeleted(deleted);
    }
}
