package com.eshop.infrastructure.persistence;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "category_closure")
@IdClass(CategoryClosureEntity.CategoryClosureId.class)
@Getter
@Setter
public class CategoryClosureEntity {
    
    @Id
    @Column(name = "ancestor_id")
    private UUID ancestorId;
    
    @Id
    @Column(name = "descendant_id")
    private UUID descendantId;
    
    private int depth;

    public static class CategoryClosureId implements Serializable {
        private UUID ancestorId;
        private UUID descendantId;
    }
}
