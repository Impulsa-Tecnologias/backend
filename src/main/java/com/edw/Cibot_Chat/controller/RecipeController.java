package com.edw.Cibot_Chat.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edw.Cibot_Chat.dto.request.CreateSavedRecipeRequest;
import com.edw.Cibot_Chat.dto.response.SavedRecipeResponse;
import com.edw.Cibot_Chat.entity.User;
import com.edw.Cibot_Chat.service.SavedRecipeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/recipes/saved")
@RequiredArgsConstructor
public class RecipeController {

    private final SavedRecipeService savedRecipeService;

    // POST /api/v1/recipes/saved
    @PostMapping
    public ResponseEntity<SavedRecipeResponse> create(
            @Valid @RequestBody CreateSavedRecipeRequest request,
            @AuthenticationPrincipal User actor) {
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedRecipeService.create(request, actor.getId(), request.getChatId()));
    }

    // GET /api/v1/recipes/saved
    @GetMapping
    public ResponseEntity<List<SavedRecipeResponse>> list(
            @AuthenticationPrincipal User actor) {
        
        return ResponseEntity.ok(savedRecipeService.list(actor.getId()));
    }

    // GET /api/v1/recipes/saved/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SavedRecipeResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User actor) {
        
        return ResponseEntity.ok(savedRecipeService.getById(id, actor.getId()));
    }

    // DELETE /api/v1/recipes/saved/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User actor) {
        
        savedRecipeService.delete(id, actor.getId());
        return ResponseEntity.noContent().build();
    }
    
}
