package com.edw.Cibot_Chat.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edw.Cibot_Chat.dto.request.LoginRequest;
import com.edw.Cibot_Chat.dto.request.RegisterRequest;
import com.edw.Cibot_Chat.dto.response.AuthResponse;
import com.edw.Cibot_Chat.entity.User;
import com.edw.Cibot_Chat.enums.Rol;
import com.edw.Cibot_Chat.repository.UserRepository;
import com.edw.Cibot_Chat.security.JwtService;
import com.edw.Cibot_Chat.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("The email exists");
        }

        User user = new User();
        
        user.setRol(Rol.FINAL);
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAllergy(request.getAllergy());
        user.setKitchenLevel(request.getKitchenLevel());

        userRepository.save(user);

        // Generar el Token JWT
        String jwtToken = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse();

        response.setToken(jwtToken);
        response.setEmail(user.getEmail());
        response.setRol(user.getRol().name());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwtToken = jwtService.generateToken(user);

        AuthResponse response = new AuthResponse();
        response.setToken(jwtToken);
        response.setEmail(user.getEmail());
        response.setRol(user.getRol().name());

        return response;
    }
    
}
