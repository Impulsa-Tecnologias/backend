package com.edw.Cibot_Chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edw.Cibot_Chat.entity.SavedRecipe;

public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long>{

    List<SavedRecipe> findByUserIdOrderByCreatedAtDesc(Long userId);
    
}
