package com.eshop.domain.category;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private UUID id;
    private String name;
    private UUID parentId;
    
    private boolean isSystem;
    
    private String pathNames;
    
    private boolean deleted;
    
    // Domain behavior methods to be implemented
}
