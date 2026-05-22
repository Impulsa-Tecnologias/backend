package com.edw.Cibot_Chat.entity;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table (name = "saved_recipe")
@Entity
@Getter
@Setter
public class SavedRecipe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, updatable = false)
    private String recipeTitle;

    @Column(nullable = false, updatable = false)
    private String recipeContent;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
    }

}
