package com.edw.Cibot_Chat.service.impl;

import java.util.List;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.edw.Cibot_Chat.dto.request.CreateUserRequest;
import com.edw.Cibot_Chat.dto.request.RegisterRequest;
import com.edw.Cibot_Chat.dto.request.UpdateUserRequest;
import com.edw.Cibot_Chat.dto.response.UserResponse;
import com.edw.Cibot_Chat.entity.User;
import com.edw.Cibot_Chat.enums.Rol;
import com.edw.Cibot_Chat.exception.ResourceNotFoundException;
import com.edw.Cibot_Chat.repository.UserRepository;
import com.edw.Cibot_Chat.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse created(CreateUserRequest request){
        User us = new User();

        us.setRol(request.getRol());
        us.setEmail(request.getEmail());
        us.setPassword(passwordEncoder.encode(request.getPassword()));
        us.setAllergy(request.getAllergy());
        us.setKitchenLevel(request.getKitchenLevel());

        return toResponse(repository.save(us));
    }

    @Override
    @Transactional
    public UserResponse createdByAdmin(RegisterRequest request){
        User us = new User();

        us.setRol(Rol.FINAL);
        us.setEmail(request.getEmail());
        us.setPassword(passwordEncoder.encode(request.getPassword()));
        us.setAllergy(request.getAllergy());
        us.setKitchenLevel(request.getKitchenLevel());

        return toResponse(repository.save(us));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAllFinalAndAdminUsers(){
        List<Rol> roles = Arrays.asList(Rol.FINAL, Rol.ADMIN);

        return repository.findByRolIn(roles).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAllFinalUsers(){
        return repository.findByRol(Rol.FINAL).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id){
        User us = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));
        return toResponse(us);
    }

    @Override
    @Transactional
    public UserResponse updateMyProfile(String email, UpdateUserRequest request) {
        User us = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            us.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getAllergy() != null) {
            us.setAllergy(request.getAllergy().trim().isEmpty() ? null : request.getAllergy());
        }
        if (request.getKitchenLevel() != null) {
            us.setKitchenLevel(request.getKitchenLevel());
        }

        return toResponse(repository.save(us));
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request, Rol actorRol){
        User us = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not Found"));
        
        // ADMIN solo puede editar usuarios FINAL
        if (actorRol == Rol.ADMIN && us.getRol() != Rol.FINAL) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Un ADMIN solo puede modificar usuarios FINAL.");
        }
        
        //  MASTER no debería modificar a otro MASTER
        if (actorRol == Rol.MASTER && us.getRol() == Rol.MASTER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede modificar a otro administrador MASTER.");
        }

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            us.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getAllergy() != null) {
            us.setAllergy(request.getAllergy().trim().isEmpty() ? null : request.getAllergy());
        }
        if (request.getKitchenLevel() != null) {
            us.setKitchenLevel(request.getKitchenLevel());
        }

        return toResponse(repository.save(us));
    }

    @Override
    @Transactional
    public void delete(Long id, Rol actorRol){
        User us = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not Found"));
        
        // ADMIN solo puede eliminar usuarios FINAL
        if (actorRol == Rol.ADMIN && us.getRol() != Rol.FINAL) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Un ADMIN solo puede eliminar usuarios FINAL.");
        }
        
        //  MASTER no debería eliminar a otro MASTER
        if (actorRol == Rol.MASTER && us.getRol() == Rol.MASTER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No se puede eliminar a otro administrador MASTER.");
        }        

        repository.deleteById(id);
    }

    private UserResponse toResponse(User us){
        UserResponse r = new UserResponse();

        r.setId(us.getId());
        r.setRol(us.getRol());
        r.setEmail(us.getEmail());
        r.setAllergy(us.getAllergy());
        r.setKitchenLevel(us.getKitchenLevel());
        r.setCreatedAt(us.getCreatedAt());
        r.setUpdateAt(us.getUpdateAt());

        return r;
    }
    
}
