package com.edw.Cibot_Chat.service.impl;

import com.edw.Cibot_Chat.dto.request.MessageRequest;
import com.edw.Cibot_Chat.dto.response.MessageResponse;
import com.edw.Cibot_Chat.dto.response.SendMessageResponse;
import com.edw.Cibot_Chat.entity.Chat;
import com.edw.Cibot_Chat.entity.Message;
import com.edw.Cibot_Chat.enums.MessageSender;
import com.edw.Cibot_Chat.exception.ResourceNotFoundException;
import com.edw.Cibot_Chat.repository.ChatRepository;
import com.edw.Cibot_Chat.repository.MessageRepository;
import com.edw.Cibot_Chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String BOT_PLACEHOLDER =
            "Respuesta del bot pendiente de integración con el servicio de IA.";

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public SendMessageResponse sendMessage(Long chatId, MessageRequest request, Long userId) {
        Chat chat = getChatOwnedByUser(chatId, userId);

        Message userMessage = Message.builder()
                .chat(chat)
                .sender(MessageSender.USUARIO)
                .content(request.getContent().trim())
                .build();

        Message botMessage = Message.builder()
                .chat(chat)
                .sender(MessageSender.BOT)
                .content(BOT_PLACEHOLDER)
                .build();

        userMessage = messageRepository.save(userMessage);
        botMessage = messageRepository.save(botMessage);

        return SendMessageResponse.builder()
                .userMessage(mapToResponse(userMessage))
                .botMessage(mapToResponse(botMessage))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> findByChat(Long chatId, Long userId) {
        getChatOwnedByUser(chatId, userId);
        return messageRepository.findByChat_IdOrderBySendDateAsc(chatId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private Chat getChatOwnedByUser(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat no encontrado con id: " + chatId));

        if (!chat.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a este chat");
        }
        return chat;
    }

    private MessageResponse mapToResponse(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .chatId(message.getChat().getId())
                .sender(message.getSender())
                .content(message.getContent())
                .sendDate(message.getSendDate())
                .build();
    }
}
