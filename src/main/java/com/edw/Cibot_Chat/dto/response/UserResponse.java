package com.edw.Cibot_Chat.dto.response;

import java.time.Instant;

import com.edw.Cibot_Chat.enums.KitchenLevel;
import com.edw.Cibot_Chat.enums.Rol;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    
    private Long id;
    private Rol rol;
    private String email;
    private String allergy;
    private KitchenLevel kitchenLevel;
    private Instant createdAt;
    private Instant updateAt;

}
