package com.edw.Cibot_Chat.service;

import java.util.List;

import com.edw.Cibot_Chat.dto.request.CreateSavedRecipeRequest;
import com.edw.Cibot_Chat.dto.response.SavedRecipeResponse;

public interface SavedRecipeService {
    
    SavedRecipeResponse create(CreateSavedRecipeRequest request);
    List<SavedRecipeResponse> list();
    SavedRecipeResponse getById(Long id);
    void delete(Long id);

}
