package com.edw.Cibot_Chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSavedRecipeRequest {

    @NotBlank(message = "Recipe title is required")
    @Size(max = 255, message = "Recipe title must be <= 255 chars")
    private String recipeTitle;

    @NotBlank(message = "Recipe content is required")
    private String recipeContent;
    
}
