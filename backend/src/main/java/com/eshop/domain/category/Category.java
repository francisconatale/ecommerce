package com.eshop.domain.category;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {
    private UUID id;
    private String name;
    private UUID parentId;
    private boolean isSystem;
    private String pathNames;
    
    // Domain behavior methods to be implemented
}
