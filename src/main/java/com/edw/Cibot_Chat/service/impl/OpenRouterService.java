package com.edw.Cibot_Chat.service.impl;

import com.edw.Cibot_Chat.dto.request.OpenRouterMessage;
import com.edw.Cibot_Chat.dto.request.OpenRouterRequest;
import com.edw.Cibot_Chat.dto.response.OpenRouterResponse;
import com.edw.Cibot_Chat.entity.Chat;
import com.edw.Cibot_Chat.entity.Message;
import com.edw.Cibot_Chat.entity.User;
import com.edw.Cibot_Chat.enums.MessageSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenRouterService {

    private final RestClient restClient;

    @Value("${openrouter.api.model}")
    private String model;

    public OpenRouterService(
            @Value("${openrouter.api.url}") String apiUrl,
            @Value("${openrouter.api.key}") String apiKey) {
        
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("HTTP-Referer", "http://localhost:3000") 
                .defaultHeader("X-Title", "Cibot-Chat")
                .build();
    }

    public String getChatCompletion(User user, Chat chat, List<Message> history) {
        List<OpenRouterMessage> apiMessages = new ArrayList<>();

        String systemPrompt = String.format(
            "Eres Cibot-Chat, un chef experto y nutricionista empático. " +
            "El objetivo de este chat es: %s. " +
            "IMPORTANTE: El usuario tiene las siguientes alergias: %s. ¡NUNCA uses estos ingredientes! " +
            "El nivel de cocina del usuario es: %s. Adapta las recetas a este nivel. " +
            "Responde en español de forma estructurada y concisa.",
            chat.getFoodObjective(),
            user.getAllergy() != null ? user.getAllergy() : "Ninguna",
            user.getKitchenLevel() != null ? user.getKitchenLevel().name() : "Desconocido"
        );
        apiMessages.add(new OpenRouterMessage("system", systemPrompt));

        if (history != null){
            for (Message msg : history) {
                String role = (msg.getSender() == MessageSender.USUARIO) ? "user" : "assistant";
                apiMessages.add(new OpenRouterMessage(role, msg.getContent()));
            }
        }

        OpenRouterRequest requestPayload = new OpenRouterRequest(model, apiMessages);

        try {
            OpenRouterResponse response = restClient.post()
                    .body(requestPayload)
                    .retrieve()
                    .body(OpenRouterResponse.class);

            if (response != null && !response.choices().isEmpty()) {
                return response.choices().get(0).message().content();
            }
            return "Lo siento, no pude procesar la respuesta en este momento.";

        } catch (Exception e) {
            System.err.println("Error llamando a OpenRouter: " + e.getMessage());
            return "Lo siento, estoy teniendo problemas de conexión con mi servidor principal. Intenta de nuevo en unos segundos.";
        }
    }
}