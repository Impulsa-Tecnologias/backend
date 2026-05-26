package com.edw.Cibot_Chat.dto.request;

import com.edw.Cibot_Chat.enums.KitchenLevel;

import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Size(max = 255, message = "password must be <= 255")
    private String password;

    private String allergy;
    private KitchenLevel kitchenLevel;
    
}
