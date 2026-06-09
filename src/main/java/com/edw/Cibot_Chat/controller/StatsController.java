package com.edw.Cibot_Chat.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.edw.Cibot_Chat.dto.response.SystemMetricsResponse;
import com.edw.Cibot_Chat.repository.ChatRepository;
import com.edw.Cibot_Chat.repository.SavedRecipeRepository;
import com.edw.Cibot_Chat.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatsController {
    
    @Value("${openrouter.api.key}")
    private String openrouterKey;

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final SavedRecipeRepository savedRecipeRepository;

    @GetMapping("/ia-usage")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MASTER')")
    public ResponseEntity<Object> getIaUsage() {
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openrouterKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://openrouter.ai/api/v1/auth/key";
        
        try {
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al consultar OpenRouter");
        }
    }

    @GetMapping("/system")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MASTER')")
    public ResponseEntity<SystemMetricsResponse> getSystemMetrics() {
        long totalUsers = userRepository.count();
        long activeChats = chatRepository.count();
        long savedRecipes = savedRecipeRepository.count();

        SystemMetricsResponse response = new SystemMetricsResponse();
        response.setTotalUsers(totalUsers);
        response.setActiveChats(activeChats);
        response.setSavedRecipes(savedRecipes);

        System.out.println("Métricas del sistema: " + response);
    
        return ResponseEntity.ok(response);
    }
}
