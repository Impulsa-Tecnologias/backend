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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = true)
    private Chat chat;

    @Column(nullable = false, length = 255, updatable = false)
    private String recipeTitle;

    @Column(nullable = false, updatable = false, columnDefinition = "TEXT")
    private String recipeContent;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
    }

}
