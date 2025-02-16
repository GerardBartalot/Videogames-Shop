package com.example.Auth.Pr2.controllers.impl;

import com.example.Auth.Pr2.commons.dtos.TokenResponse;
import com.example.Auth.Pr2.commons.dtos.UserRequest;
import com.example.Auth.Pr2.commons.entities.UserModel;
import com.example.Auth.Pr2.controllers.AuthApi;
import com.example.Auth.Pr2.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<TokenResponse> registerUser(UserRequest userRequest) {
        return ResponseEntity.ok(authService.registerUser(userRequest));
    }

    @Override
    public ResponseEntity<TokenResponse> loginUser(UserRequest userRequest) {
        return ResponseEntity.ok(authService.loginUser(userRequest));
    }

    @Override
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

}