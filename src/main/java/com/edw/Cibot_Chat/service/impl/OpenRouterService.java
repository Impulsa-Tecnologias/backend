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

        String userName = user.getEmail() != null ? user.getEmail().split("@")[0] : "Chef";

        String systemPrompt = String.format(
            "Eres Cibot-Chat, un chef experto y nutricionista empático y tu nombre es Cibot. " +
            "Tu interlocutor es un usuario real llamado %s. Dirígete a él por su nombre de forma natural, NUNCA uses marcadores genéricos como '[PERSON_NAME]' o '[USUARIO]'. " +
            "REGLA DE ORO: Tu único propósito es ayudar al usuario con temas de alimentación, nutrición, recetas, técnicas de cocina y la gestión de sus propios datos nutricionales. " +
            "Si el usuario te pregunta sobre sus propias alergias, sus objetivos o su nivel de cocina, DEBES responderle amigablemente confirmando que conoces dicha información. " +
            "Únicamente si el usuario te pregunta sobre temas totalmente ajenos (como programación, política, historia, etc.), " +
            "DEBES negarte educadamente diciendo exactamente: 'Lo siento, como asistente nutricional de Cibot-Chat solo puedo ayudarte con temas de alimentación y cocina.' y NO respondas a la pregunta original. " + 
            "Información actual del usuario para este chat:\n" +
            "- Su objetivo en este chat: %s.\n" +
            "- Sus alergias: %s (¡NUNCA uses estos ingredientes!).\n" +
            "- Su nivel de cocina actual: %s (adapta la complejidad a este nivel).\n\n" +
            "SI VAS A ENTREGAR UNA RECETA, DEBES formatear tu respuesta EXACTAMENTE de la siguiente manera:\n" +
            "TÍTULO: [Nombre de la receta]\n" +
            "CONTENIDO:\n" +
            "[Todo el paso a paso y los ingredientes aquí]\n\n" +
            "Responde en español de forma estructurada y concisa.",
            userName,
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