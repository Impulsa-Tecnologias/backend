package com.edw.Cibot_Chat.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.edw.Cibot_Chat.dto.request.CreateUserRequest;
import com.edw.Cibot_Chat.dto.request.UpdateUserRequest;
import com.edw.Cibot_Chat.dto.response.UserResponse;
import com.edw.Cibot_Chat.entity.User;
import com.edw.Cibot_Chat.enums.Rol;
import com.edw.Cibot_Chat.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // PUT /api/v1/users/profile
    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateMyProfile(
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal User actor) { // Spring Security inyecta al usuario dueño del Token
        
        return ResponseEntity.ok(userService.updateMyProfile(actor.getEmail(), request));
    }

    // GET /api/v1/users
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MASTER')")
    public ResponseEntity<List<UserResponse>> listUsers(@AuthenticationPrincipal User actor) {
        if (actor.getRol() == Rol.MASTER) {
            return ResponseEntity.ok(userService.findAllFinalAndAdminUsers());
        } else {
            return ResponseEntity.ok(userService.findAllFinalUsers());
        }
    }

    // POST /api/v1/users
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MASTER')")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request,
            @AuthenticationPrincipal User actor) {

        // Un ADMIN solo puede crear usuarios FINAL
        if (actor.getRol() == Rol.ADMIN) {
            request.setRol(Rol.FINAL);
        }
        
        // Nadie puede crear un MASTER por API.
        if (request.getRol() == Rol.MASTER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado para crear roles MASTER");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.created(request));
    }

    // PUT /api/v1/users/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MASTER')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request,
            @AuthenticationPrincipal User actor) {
        
        return ResponseEntity.ok(userService.update(id, request, actor.getRol()));
    }

    // DELETE /api/v1/users/{id}
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MASTER')")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal User actor) {
        
        userService.delete(id, actor.getRol());
        return ResponseEntity.noContent().build();
    }
    
}
