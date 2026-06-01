package com.edw.Cibot_Chat.dto.request;

public record OpenRouterMessage(
        // Role puede ser "system", "user" o "assistant"
        String role,
        String content 
) {}