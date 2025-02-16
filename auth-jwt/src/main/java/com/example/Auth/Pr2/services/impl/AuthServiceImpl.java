package com.example.Auth.Pr2.services.impl;


import com.example.Auth.Pr2.commons.dtos.TokenResponse;
import com.example.Auth.Pr2.commons.dtos.UserRequest;
import com.example.Auth.Pr2.commons.entities.UserModel;
import com.example.Auth.Pr2.commons.enums.Role;
import com.example.Auth.Pr2.repositories.UserRepository;
import com.example.Auth.Pr2.services.AuthService;
import com.example.Auth.Pr2.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public TokenResponse registerUser(UserRequest userRequest) {
        Role userRole = userRepository.count() < 1 ? Role.ADMIN : Role.USER;

        UserModel user = new UserModel(
                userRequest.getEmail(),
                passwordEncoder.encode(userRequest.getPassword()),
                userRole
        );

        userRepository.save(user);

        kafkaTemplate.send("user.registered", user.getId().toString()); // Enviar userId en Kafka

        return jwtService.generateToken(user.getId());
    }

    @Override
    public TokenResponse loginUser(UserRequest userRequest) {
        UserModel user = userRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtService.generateToken(user.getId());
    }

    @Override
    public List<UserModel> getAllUsers() {
        return List.of();
    }
}

