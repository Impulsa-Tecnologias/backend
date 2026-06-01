package com.edw.Cibot_Chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChatRequest {

    @Size(max = 100, message = "Name must be <= 100")
    private String name = "Nuevo Chat";

    @NotBlank(message = "Objective is required")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+$", message = "The target can only contain letters and numbers")
    private String foodObjective;
}
