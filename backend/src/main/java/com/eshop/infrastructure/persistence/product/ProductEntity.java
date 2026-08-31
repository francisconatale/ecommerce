package com.eshop.infrastructure.persistence.product;
import com.eshop.infrastructure.persistence.base.JpaBaseEntity;
import com.eshop.infrastructure.persistence.base.JpaBaseEntity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
@Getter
@Setter
public class ProductEntity {
    @Id
    private UUID id;
    private String name;
    
    @Column(name = "price_buy")
    private BigDecimal priceBuy;
    
    @Column(name = "price_sell")
    private BigDecimal priceSell;
    
    @Column(name = "category_id")
    private UUID categoryId;
    
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
