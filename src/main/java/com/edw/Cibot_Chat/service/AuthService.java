package com.edw.Cibot_Chat.service;

import com.edw.Cibot_Chat.dto.request.LoginRequest;
import com.edw.Cibot_Chat.dto.request.RegisterRequest;
import com.edw.Cibot_Chat.dto.response.AuthResponse;

public interface AuthService {
    
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

}
