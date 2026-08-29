package com.eshop.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "category")
@SQLDelete(sql = "UPDATE category SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Getter
@Setter
public class CategoryEntity {
    @Id
    private UUID id;
    private String name;
    
    @Column(name = "parent_id")
    private UUID parentId;
    
    @Column(name = "is_system")
    private boolean isSystem;
    
    @Column(name = "path_names")
    private String pathNames;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    private boolean deleted;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
