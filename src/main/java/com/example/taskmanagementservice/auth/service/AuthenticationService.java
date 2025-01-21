package com.example.taskmanagementservice.auth.service;


import com.example.taskmanagementservice.exception.AuthenticationException;
import com.example.taskmanagementservice.exception.DuplicateException;
import com.example.taskmanagementservice.jwt.service.JWTService;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.repository.UserRepository;
import com.example.taskmanagementservice.user.service.UserService;
import com.example.taskmanagementservice.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;

    private final JWTService jwtService;

    private final AuthenticationManager authManager;

    private final PasswordEncoder passwordEncoder;

    public User register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.createUser(user);
    }

    public String login(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername(),
                    new HashMap<>());
        }

        // TODO Отслеживается ли это исключение?
        throw new AuthenticationException("Invalid username or password");
    }
}
