package com.eshop.domain.category;

import com.eshop.domain.base.BaseEntity;
import java.util.Objects;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import com.eshop.domain.exception.SystemCategoryImmutableException;

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

    public Category(String name, UUID parentId) {
        this.name = name;
        this.parentId = parentId;
        this.isSystem = false;
    }

    public boolean isRoot() {
        return this.parentId == null;
    }

    public void validateNotSystem() {
        if (this.isSystem) {
            throw new SystemCategoryImmutableException("Cannot update a system category");
        }
    }

    public boolean rename(String newName) {
        validateNotSystem();
        if (!this.name.equals(newName)) {
            this.name = newName;
            return true;
        }
        return false;
    }

    public boolean moveToParent(UUID newParentId) {
        validateNotSystem();
        if (!Objects.equals(this.parentId, newParentId)) {
            this.parentId = newParentId;
            return true;
        }
        return false;
    }

    public void recalculatePath(Category parent) {
        if (this.isRoot()) {
            this.pathNames = this.name;
        } else {
            this.pathNames = parent.getPathNames() + " > " + this.name;
        }
    }

    public void replacePathPrefix(String oldPrefix, String newPrefix) {
        if (this.pathNames != null && oldPrefix != null && this.pathNames.startsWith(oldPrefix)) {
            this.pathNames = newPrefix + this.pathNames.substring(oldPrefix.length());
        }
    }

    public void removeDeletedParentFromPath(String deletedParentName) {
        if (this.pathNames != null) {
            this.pathNames = this.pathNames.replace(deletedParentName + " > ", "");
        }
    }

    public void markAsSystem() {
        this.isSystem = true;
    }
}
