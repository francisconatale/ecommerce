package com.eshop.domain.category;

import java.util.UUID;

public class Category {
    private UUID id;
    private String name;
    private UUID parentId;
    private boolean isSystem;
    private String pathNames;
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public UUID getParentId() { return parentId; }
    public void setParentId(UUID parentId) { this.parentId = parentId; }
    
    public boolean isSystem() { return isSystem; }
    public void setSystem(boolean system) { isSystem = system; }
    
    public String getPathNames() { return pathNames; }
    public void setPathNames(String pathNames) { this.pathNames = pathNames; }
    
    // Domain behavior methods to be implemented
}
