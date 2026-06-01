package com.edw.Cibot_Chat.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavedRecipeResponse {
    
    private Long id;
    private Long userId;
    private Long chatId;
    private String recipeTitle;
    private String recipeContent;
    private Instant createdAt;

}
