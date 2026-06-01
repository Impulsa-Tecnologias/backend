package com.edw.Cibot_Chat.service;

import java.util.List;

import com.edw.Cibot_Chat.dto.request.CreateUserRequest;
import com.edw.Cibot_Chat.dto.request.RegisterRequest;
import com.edw.Cibot_Chat.dto.request.UpdateUserRequest;
import com.edw.Cibot_Chat.dto.response.UserResponse;
import com.edw.Cibot_Chat.enums.Rol;

public interface UserService {

    UserResponse created(CreateUserRequest request);
    UserResponse createdByAdmin(RegisterRequest request);
    List<UserResponse> findAllFinalAndAdminUsers();
    List<UserResponse> findAllFinalUsers();
    UserResponse getById(Long id);
    UserResponse updateMyProfile(String email, UpdateUserRequest request);
    UserResponse update(Long id, UpdateUserRequest request, Rol actorRol);
    void delete(Long id, Rol actorRol);
    
}
