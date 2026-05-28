package com.edw.Cibot_Chat.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.edw.Cibot_Chat.dto.request.CreateSavedRecipeRequest;
import com.edw.Cibot_Chat.dto.response.SavedRecipeResponse;
import com.edw.Cibot_Chat.entity.Chat;
import com.edw.Cibot_Chat.entity.SavedRecipe;
import com.edw.Cibot_Chat.entity.User;
import com.edw.Cibot_Chat.exception.ResourceNotFoundException;
import com.edw.Cibot_Chat.repository.ChatRepository;
import com.edw.Cibot_Chat.repository.SavedRecipeRepository;
import com.edw.Cibot_Chat.repository.UserRepository;
import com.edw.Cibot_Chat.service.SavedRecipeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SavedRecipeServiceImpl implements SavedRecipeService{

    private final SavedRecipeRepository repository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SavedRecipeResponse create(CreateSavedRecipeRequest request, Long userId, Long chatId){
        SavedRecipe sr = new SavedRecipe();

        sr.setRecipeTitle(request.getRecipeTitle());
        sr.setRecipeContent(request.getRecipeContent());

        User us = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
        
        sr.setUser(us);

        if (chatId != null) {
            Chat chat = chatRepository.findById(chatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Chat "+ chatId + " not found"));

            // Validamos que el chat le pertenezca a este usuario
            if (!chat.getUser().getId().equals(us.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a este chat");
            }
            
            sr.setChat(chat);
        }

        return toResponse(repository.save(sr));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedRecipeResponse> list(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SavedRecipeResponse getById(Long id, Long userId) {
        SavedRecipe sr = getRecipeOwnedByUser(id, userId);

        return toResponse(sr);
    }

    @Override
    public void delete(Long id, Long userId) {
        SavedRecipe sr = getRecipeOwnedByUser(id, userId);

        repository.delete(sr);
    }

    private SavedRecipe getRecipeOwnedByUser(Long recipeId, Long userId) {
        SavedRecipe sr = repository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe " + recipeId + " not found"));
        
        if (!sr.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso sobre esta receta");
        }
        return sr;
    }

    private SavedRecipeResponse toResponse(SavedRecipe sr) {
        SavedRecipeResponse r = new SavedRecipeResponse();

        r.setId(sr.getId());
        r.setUserId(sr.getUser().getId());
        r.setChatId(sr.getChat() != null ? sr.getChat().getId() : null);
        r.setRecipeTitle(sr.getRecipeTitle());
        r.setRecipeContent(sr.getRecipeContent());
        r.setCreatedAt(sr.getCreatedAt());

        return r;
    }
    
}
