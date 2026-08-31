package com.eshop.infrastructure.persistence.category;
import com.eshop.infrastructure.persistence.base.JpaBaseEntity;
import com.eshop.infrastructure.persistence.base.JpaBaseEntity;

import jakarta.persistence.*;
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
public class CategoryEntity extends JpaBaseEntity {
    private String name;
    
    @Column(name = "parent_id")
    private UUID parentId;
    
    @Column(name = "is_system")
    private boolean isSystem;
    
    @Column(name = "path_names")
    private String pathNames;
}
