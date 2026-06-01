package com.edw.Cibot_Chat.controller;

import com.edw.Cibot_Chat.dto.request.ChatRequest;
import com.edw.Cibot_Chat.dto.request.MessageRequest;
import com.edw.Cibot_Chat.dto.request.UpdateChatRequest;
import com.edw.Cibot_Chat.dto.response.ChatResponse;
import com.edw.Cibot_Chat.dto.response.MessageResponse;
import com.edw.Cibot_Chat.entity.User;
import com.edw.Cibot_Chat.service.ChatService;
import com.edw.Cibot_Chat.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    // POST /api/v1/chats
    @PostMapping
    public ResponseEntity<ChatResponse> create(
            @Valid @RequestBody ChatRequest request,
            @AuthenticationPrincipal User actor) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chatService.create(request, actor.getId()));
    }

    // PUT /api/v1/chats/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ChatResponse> updateChat(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChatRequest request,
            @AuthenticationPrincipal User actor) {
        
        return ResponseEntity.ok(chatService.update(request, id, actor.getId()));
    }

    // GET /api/v1/chats
    @GetMapping
    public ResponseEntity<List<ChatResponse>> findAll(
            @AuthenticationPrincipal User actor) {

        return ResponseEntity.ok(chatService.findAllByUser(actor.getId()));
    }

    // DELETE /api/v1/chats/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User actor) {

        chatService.delete(id, actor.getId());
        return ResponseEntity.noContent().build();
    }

    // Mensajes

    // POST /api/v1/chats/{id}/messages
    @PostMapping("/{id}/messages")
    public ResponseEntity<MessageResponse> sendMessage(
            @PathVariable Long id,
            @Valid @RequestBody MessageRequest request,
            @AuthenticationPrincipal User actor) {

        // Retorna la respuesta del BOT generada en el servicio
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.sendMessage(id, request, actor.getId()));
    }

    // GET /api/v1/chats/{id}/messages
    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageResponse>> findMessages(
            @PathVariable Long id,
            @AuthenticationPrincipal User actor) {

        return ResponseEntity.ok(messageService.listAllMessageByChat(id, actor.getId()));
    }
}
