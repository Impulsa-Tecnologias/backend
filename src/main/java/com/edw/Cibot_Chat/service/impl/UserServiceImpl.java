package com.edw.Cibot_Chat.service.impl;

import java.util.List;
import java.util.Arrays;

import org.springframework.transaction.annotation.Transactional;

import com.edw.Cibot_Chat.dto.CreateUserRequest;
import com.edw.Cibot_Chat.dto.UpdateUserRequest;
import com.edw.Cibot_Chat.dto.UserResponse;
import com.edw.Cibot_Chat.entity.Rol;
import com.edw.Cibot_Chat.entity.User;
import com.edw.Cibot_Chat.exception.ResourceNotFoundException;
import com.edw.Cibot_Chat.repository.UserRepository;
import com.edw.Cibot_Chat.service.UserService;

public class UserServiceImpl implements UserService{

    private final UserRepository repository;
    public UserServiceImpl(UserRepository repository){
        this.repository = repository;
    }

    @Override
    public UserResponse created(CreateUserRequest request){
        User us = new User();

        us.setRol(request.getRol());
        us.setEmail(request.getEmail());
        us.setPassword(request.getPassword());
        us.setAllergy(request.getAllergy());
        us.setKitchenLevel(request.getKitchenLevel());

        User saved = repository.save(us);

        return toResponse(saved);
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
    public UserResponse update(Long id, UpdateUserRequest request){
        User us = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not Found"));
        
        if (request.getPassword() != null) {
            us.setPassword(request.getPassword());
        }

        if (request.getAllergy() != null || request.getAllergy() == "") {
            if (request.getAllergy() == "") {
                us.setAllergy(null);
            }
            us.setAllergy(request.getAllergy());
        }

        if (request.getKitchenLevel() != null) {
            us.setKitchenLevel(request.getKitchenLevel());
        }

        return toResponse(repository.save(us));
    }

    @Override
    public void delete(Long id){
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("User " + id + " not found");
        }

        repository.deleteById(id);
    }

    private UserResponse toResponse(User us){
        UserResponse r = new UserResponse();

        r.setId(us.getId());
        r.setEmail(us.getEmail());
        r.setPassword(us.getPassword());
        r.setAllergy(us.getAllergy());
        r.setKitchenLevel(us.getKitchenLevel());
        r.setCreatedAt(us.getCreatedAt());
        r.setUpdateAt(us.getUpdateAt());

        return r;
    }
    
}
