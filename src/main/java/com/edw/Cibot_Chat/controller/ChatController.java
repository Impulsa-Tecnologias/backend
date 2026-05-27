package com.edw.Cibot_Chat.controller;

import com.edw.Cibot_Chat.dto.request.ChatRequest;
import com.edw.Cibot_Chat.dto.request.MessageRequest;
import com.edw.Cibot_Chat.dto.response.ApiResponse;
import com.edw.Cibot_Chat.dto.response.ChatResponse;
import com.edw.Cibot_Chat.dto.response.MessageResponse;
import com.edw.Cibot_Chat.service.ChatService;
import com.edw.Cibot_Chat.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> create(
            @Valid @RequestBody ChatRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Chat creado exitosamente",
                        chatService.create(request, resolveUserId(currentUser))));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatResponse>>> findAll(
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(ApiResponse.ok(
                chatService.findAllByUser(resolveUserId(currentUser))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails currentUser) {
        chatService.delete(id, resolveUserId(currentUser));
        return ResponseEntity.ok(ApiResponse.ok("Chat eliminado", null));
    }

    @PostMapping("/{id}/messages")
    public ResponseEntity<ApiResponse<SendMessageResponse>> sendMessage(
            @PathVariable Long id,
            @Valid @RequestBody MessageRequest request,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Mensaje enviado",
                        messageService.sendMessage(id, request, resolveUserId(currentUser))));
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> findMessages(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(ApiResponse.ok(
                messageService.findByChat(id, resolveUserId(currentUser))));
    }

    /**
     * El subject del JWT debe ser el id del usuario como String.
     * Se ajustará cuando exista el módulo de autenticación con UserRepository.
     */
    private Long resolveUserId(UserDetails currentUser) {
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        try {
            return Long.parseLong(currentUser.getUsername());
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identificador de usuario inválido");
        }
    }
}
