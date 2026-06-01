package com.edw.Cibot_Chat.dto.response;

import java.util.List;

import com.edw.Cibot_Chat.dto.request.OpenRouterMessage;

public record OpenRouterResponse(
        List<Choice> choices
) {
    public record Choice(
            OpenRouterMessage message
    ) {}
}