package com.edw.Cibot_Chat.service.impl;

import com.edw.Cibot_Chat.dto.request.ChatRequest;
import com.edw.Cibot_Chat.dto.response.ChatResponse;
import com.edw.Cibot_Chat.entity.Chat;
import com.edw.Cibot_Chat.exception.ResourceNotFoundException;
import com.edw.Cibot_Chat.repository.ChatRepository;
import com.edw.Cibot_Chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private static final int MAX_CHATS_PER_USER = 10;

    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public ChatResponse create(ChatRequest request, Long userId) {
        if (chatRepository.countByUserId(userId) >= MAX_CHATS_PER_USER) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Has alcanzado el máximo de " + MAX_CHATS_PER_USER + " chats permitidos");
        }

        String name = StringUtils.hasText(request.getName()) ? request.getName().trim() : "Nuevo chat";

        Chat chat = Chat.builder()
                .userId(userId)
                .name(name)
                .foodObjective(request.getFoodObjective().trim())
                .build();

        return mapToResponse(chatRepository.save(chat));
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
                .orElseThrow(() -> new ResourceNotFoundException("Chat no encontrado con id: " + chatId));

        if (!chat.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a este chat");
        }
        return chat;
    }

    private ChatResponse mapToResponse(Chat chat) {
        return ChatResponse.builder()
                .id(chat.getId())
                .userId(chat.getUserId())
                .name(chat.getName())
                .foodObjective(chat.getFoodObjective())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
