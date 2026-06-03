package com.edw.Cibot_Chat.dto.response;

import com.edw.Cibot_Chat.enums.KitchenLevel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String token;
    private String email;
    private String rol;
    private String allergy;
    private KitchenLevel kitchenLevel;
}