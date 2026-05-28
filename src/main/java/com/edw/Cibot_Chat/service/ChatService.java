package com.edw.Cibot_Chat.service;

import com.edw.Cibot_Chat.dto.request.ChatRequest;
import com.edw.Cibot_Chat.dto.request.UpdateChatRequest;
import com.edw.Cibot_Chat.dto.response.ChatResponse;

import java.util.List;

public interface ChatService {

    ChatResponse create(ChatRequest request, Long userId);

    ChatResponse update(UpdateChatRequest request, Long userId, Long chatId);

    List<ChatResponse> findAllByUser(Long userId);

    void delete(Long chatId, Long userId);
}
