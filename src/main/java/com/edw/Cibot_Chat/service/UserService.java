package com.edw.Cibot_Chat.service;

import java.util.List;

import com.edw.Cibot_Chat.dto.CreateUserRequest;
import com.edw.Cibot_Chat.dto.UpdateUserRequest;
import com.edw.Cibot_Chat.dto.UserResponse;

public interface UserService {

    UserResponse created(CreateUserRequest request);
    List<UserResponse> findAllFinalAndAdminUsers();
    List<UserResponse> findAllFinalUsers();
    UserResponse getById(Long id);
    UserResponse update(Long id, UpdateUserRequest request);
    void delete(Long id);
    
}
