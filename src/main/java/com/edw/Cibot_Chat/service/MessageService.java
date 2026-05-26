package com.edw.Cibot_Chat.service;

import com.edw.Cibot_Chat.dto.request.MessageRequest;
import com.edw.Cibot_Chat.dto.response.MessageResponse;
import com.edw.Cibot_Chat.dto.response.SendMessageResponse;

import java.util.List;

public interface MessageService {

    SendMessageResponse sendMessage(Long chatId, MessageRequest request, Long userId);

    List<MessageResponse> findByChat(Long chatId, Long userId);
}
