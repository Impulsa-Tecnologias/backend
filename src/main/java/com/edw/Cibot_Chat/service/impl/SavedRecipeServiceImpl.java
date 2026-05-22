package com.edw.Cibot_Chat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edw.Cibot_Chat.dto.CreateSavedRecipeRequest;
import com.edw.Cibot_Chat.dto.SavedRecipeResponse;
import com.edw.Cibot_Chat.entity.SavedRecipe;
import com.edw.Cibot_Chat.exception.ResourceNotFoundException;
import com.edw.Cibot_Chat.repository.SavedRecipeRepository;
import com.edw.Cibot_Chat.service.SavedRecipeService;

@Service
@Transactional
public class SavedRecipeServiceImpl implements SavedRecipeService{

    private final SavedRecipeRepository repository;
    public SavedRecipeServiceImpl(SavedRecipeRepository repository){
        this.repository = repository;
    }

    @Override
    public SavedRecipeResponse create(CreateSavedRecipeRequest request){
        SavedRecipe sr = new SavedRecipe();

        sr.setRecipeTitle(request.getRecipeTitle());
        sr.setRecipeContent(request.getRecipeContent());

        SavedRecipe saved = repository.save(sr);

        return toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedRecipeResponse> list() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SavedRecipeResponse getById(Long id) {
        SavedRecipe sr = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Saved recipe " + id + " not found"));
        return toResponse(sr);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Saved recipe " + id + " not found");
        }

        repository.deleteById(id);
    }

    private SavedRecipeResponse toResponse(SavedRecipe sr) {
        SavedRecipeResponse r = new SavedRecipeResponse();

        r.setId(sr.getId());
        r.setRecipeTitle(sr.getRecipeTitle());
        r.setRecipeContent(sr.getRecipeContent());
        r.setCreatedAt(sr.getCreatedAt());

        return r;
    }
    
}
