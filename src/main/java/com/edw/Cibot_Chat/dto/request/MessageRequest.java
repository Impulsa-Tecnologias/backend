package com.edw.Cibot_Chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageRequest {
    
    @NotBlank(message = "Content is required")
    private String content;

}
