package com.edw.Cibot_Chat.service.impl;

import com.edw.Cibot_Chat.dto.request.ChatRequest;
import com.edw.Cibot_Chat.dto.request.UpdateChatRequest;
import com.edw.Cibot_Chat.dto.response.ChatResponse;
import com.edw.Cibot_Chat.entity.Chat;
import com.edw.Cibot_Chat.entity.User;
import com.edw.Cibot_Chat.exception.ResourceNotFoundException;
import com.edw.Cibot_Chat.repository.ChatRepository;
import com.edw.Cibot_Chat.repository.UserRepository;
import com.edw.Cibot_Chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private static final int MAX_CHATS_PER_USER = 10;

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChatResponse create(ChatRequest request, Long userId) {
        User us = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));

        if (chatRepository.countByUserId(userId) >= MAX_CHATS_PER_USER) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Has alcanzado el máximo de " + MAX_CHATS_PER_USER + " chats permitidos");
        }

        Chat chat = Chat.builder()
                .user(us)
                .name(request.getName())
                .foodObjective(request.getFoodObjective().trim())
                .build();

        return mapToResponse(chatRepository.save(chat));
    }

    @Override
    @Transactional
    public ChatResponse update(UpdateChatRequest request, Long chatId, Long userId){
        Chat chat = getChatOwnedByUser(chatId, userId);

        chat.setName(request.getName());

        return mapToResponse(chat);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatResponse> findAllByUser(Long userId) {
        return chatRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long chatId, Long userId) {
        Chat chat = getChatOwnedByUser(chatId, userId);
        chatRepository.delete(chat);
    }

    private Chat getChatOwnedByUser(Long chatId, Long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat " + chatId + " not found"));
        
        if (!chat.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access not allowed");
        }

        return chat;
    }

    private ChatResponse mapToResponse(Chat chat) {
        return ChatResponse.builder()
                .id(chat.getId())
                .userId(chat.getUser().getId())
                .name(chat.getName())
                .foodObjective(chat.getFoodObjective())
                .createdAt(chat.getCreatedAt())
                .updateTime(chat.getUpdateTime())
                .build();
    }
}
