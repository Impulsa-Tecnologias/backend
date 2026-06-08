package com.edw.Cibot_Chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edw.Cibot_Chat.entity.SavedRecipe;

public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long>{

    List<SavedRecipe> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<SavedRecipe> findByUser_IdAndChat_IdAndRecipeTitleAndRecipeContent(Long userId, Long chatId, String title, String content);
    Optional<SavedRecipe> findByUser_IdAndChat_IsNullAndRecipeTitleAndRecipeContent(Long userId, String title, String content);
    
}
