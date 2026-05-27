package com.edw.Cibot_Chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @Size(max = 255, message = "Email must be <= 255")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(max = 255, message = "password must be <= 255")
    @NotBlank(message = "Password is required")
    private String password;
}