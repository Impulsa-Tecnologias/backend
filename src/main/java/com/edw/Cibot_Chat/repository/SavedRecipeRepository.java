package com.edw.Cibot_Chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edw.Cibot_Chat.entity.SavedRecipe;

public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long>{
    
}
