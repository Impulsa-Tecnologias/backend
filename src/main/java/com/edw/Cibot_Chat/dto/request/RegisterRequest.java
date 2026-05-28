package com.edw.Cibot_Chat.dto.request;

import com.edw.Cibot_Chat.enums.KitchenLevel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Email is requerid")
    @Size(max = 255, message = "Email must be <= 255")
    private String email;

    @NotBlank(message = "password is requerid")
    @Size(max = 255, message = "password must be <= 255")
    private String password;

    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "Allergies can only contain letters")
    private String allergy;
    
    private KitchenLevel kitchenLevel = KitchenLevel.NULL;
    
}
