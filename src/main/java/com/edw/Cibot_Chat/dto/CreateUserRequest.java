package com.edw.Cibot_Chat.dto;

import com.edw.Cibot_Chat.entity.KitchenLevel;
import com.edw.Cibot_Chat.entity.Rol;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotBlank(message = "Rol is requerid")
    private Rol rol;

    @NotBlank(message = "Email is requerid")
    @Size(max = 255, message = "Email must be <= 255")
    private String email;

    @NotBlank(message = "password is requerid")
    @Size(max = 255, message = "password must be <= 255")
    private String password;

    private String allergy;
    private KitchenLevel kitchenLevel = KitchenLevel.NULL;
    
}
