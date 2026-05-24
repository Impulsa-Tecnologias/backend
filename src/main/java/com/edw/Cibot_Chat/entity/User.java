package com.edw.Cibot_Chat.entity;

import java.time.Instant;

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

    @Column()
    private String allergy;

    @Column(nullable = false)
    private KitchenLevel kitchenLevel;

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = false, name = "update_at")
    private Instant updateAt;

    @PrePersist
    void onCreated() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updateAt = now;
    }

    @PrePersist
    void onUpdate() {
        this.updateAt = Instant.now();
    }


}
