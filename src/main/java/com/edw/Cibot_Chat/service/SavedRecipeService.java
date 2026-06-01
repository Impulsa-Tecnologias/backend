package com.edw.Cibot_Chat.service;

import java.util.List;

import com.edw.Cibot_Chat.dto.request.CreateSavedRecipeRequest;
import com.edw.Cibot_Chat.dto.response.SavedRecipeResponse;

public interface SavedRecipeService {
    
    SavedRecipeResponse create(CreateSavedRecipeRequest request, Long userId, Long chatId);
    List<SavedRecipeResponse> list(Long userId);
    SavedRecipeResponse getById(Long id, Long userId);
    void delete(Long id, Long userId);

}
