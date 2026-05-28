package com.edw.Cibot_Chat.service;

import com.edw.Cibot_Chat.dto.request.MessageRequest;
import com.edw.Cibot_Chat.dto.response.MessageResponse;

import java.util.List;

public interface MessageService {

    MessageResponse sendMessage(Long chatId, MessageRequest request, Long userId);

    List<MessageResponse> listAllMessageByChat(Long chatId, Long userId);
}
