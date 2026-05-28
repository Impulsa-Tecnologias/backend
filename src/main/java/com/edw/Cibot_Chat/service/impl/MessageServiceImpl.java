package com.edw.Cibot_Chat.service.impl;

import com.edw.Cibot_Chat.dto.request.MessageRequest;
import com.edw.Cibot_Chat.dto.response.MessageResponse;
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

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public MessageResponse sendMessage(Long chatId, MessageRequest request, Long userId) {
        Chat chat = getChatOwnedByUser(chatId, userId);

        Message message = Message.builder()
                .chat(chat)
                .sender(MessageSender.USUARIO)
                .content(request.getContent())
                .build();

        messageRepository.save(message);

        // Metodo API 

        String botResponseText = "¡Hola! He analizado tu objetivo de " + chat.getFoodObjective() + 
                                 " y esta es mi recomendación...";

        Message boot = Message.builder()
                .chat(chat)
                .sender(MessageSender.BOT)
                .content(botResponseText)
                .build();

        Message save = messageRepository.save(boot);

        return mapToResponse(save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> listAllMessageByChat(Long chatId, Long userId) {
        getChatOwnedByUser(chatId, userId);
        return messageRepository.findByChat_IdOrderBySendDateAsc(chatId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private Chat getChatOwnedByUser(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat " + chatId + " not found"));
        
        if (!chat.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access not allowed");
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
