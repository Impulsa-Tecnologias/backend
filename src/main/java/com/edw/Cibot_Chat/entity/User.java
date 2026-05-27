package com.edw.Cibot_Chat.entity;

import java.time.Instant;
import java.util.List;

import com.edw.Cibot_Chat.enums.KitchenLevel;
import com.edw.Cibot_Chat.enums.Rol;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = false, length = 255)
    private String email;
    
    @Column(nullable = false, length = 255)
    private String password;

    @Column(columnDefinition = "TEXT")
    private String allergy;

    @Column(nullable = false)
    private KitchenLevel kitchenLevel;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedRecipe> recipes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats;

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = false, name = "update_at")
    private Instant updateAt;

    @PrePersist
    protected void onCreated() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updateAt = now;
    }

    @PrePersist
    protected void onUpdate() {
        this.updateAt = Instant.now();
    }


}
