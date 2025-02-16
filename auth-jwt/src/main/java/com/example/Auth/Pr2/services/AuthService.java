package com.example.Auth.Pr2.services;

import com.example.Auth.Pr2.commons.dtos.TokenResponse;
import com.example.Auth.Pr2.commons.dtos.UserRequest;
import com.example.Auth.Pr2.commons.entities.UserModel;

import java.util.List;

public interface AuthService {
    TokenResponse registerUser(UserRequest userRequest);

    TokenResponse loginUser(UserRequest userRequest);

    List<UserModel> getAllUsers();
}
