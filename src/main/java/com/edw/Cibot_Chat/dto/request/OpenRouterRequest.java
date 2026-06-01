package com.edw.Cibot_Chat.dto.request;

import java.util.List;

public record OpenRouterRequest(
        String model,
        List<OpenRouterMessage> messages
) {}