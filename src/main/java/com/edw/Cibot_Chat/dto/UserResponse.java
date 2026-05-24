package com.edw.Cibot_Chat.dto;

import java.time.Instant;

import com.edw.Cibot_Chat.entity.KitchenLevel;
import com.edw.Cibot_Chat.entity.Rol;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    
    private Long id;
    private Rol rol;
    private String email;
    private String password;
    private String allergy;
    private KitchenLevel kitchenLevel;
    private Instant createdAt;
    private Instant updateAt;

}
