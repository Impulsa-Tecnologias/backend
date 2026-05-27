package com.edw.Cibot_Chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateChatRequest {

    @NotBlank
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String name;
    
}
