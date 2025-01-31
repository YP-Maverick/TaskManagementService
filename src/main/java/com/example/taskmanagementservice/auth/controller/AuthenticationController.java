package com.example.taskmanagementservice.auth.controller;

import com.example.taskmanagementservice.auth.request.AuthenticationRequest;
import com.example.taskmanagementservice.auth.request.AuthenticationResponse;
import com.example.taskmanagementservice.auth.request.RegistrationRequest;
import com.example.taskmanagementservice.auth.request.RegistrationResponse;
import com.example.taskmanagementservice.auth.service.AuthenticationService;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.model.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;


    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(
            @Valid @RequestBody RegistrationRequest request) {

        User user = userMapper.toUser(request);
        return ResponseEntity.ok(
                userMapper.toRegistrationResponse(
                        authenticationService.register(user)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody AuthenticationRequest request) {

        User user = userMapper.toUser(request);
        return ResponseEntity.ok(
                authenticationService.login(user)
        );
    }

    // TODO Обновление токена
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request) {

        return  ResponseEntity.ok(
                authenticationService.refreshToken(request)
        );
    }
}
