package com.example.taskmanagementservice.auth.service;


import com.example.taskmanagementservice.auth.request.AuthenticationResponse;
import com.example.taskmanagementservice.exception.AuthenticationException;
import com.example.taskmanagementservice.exception.InvalidTokenException;
import com.example.taskmanagementservice.exception.InvalidTokenFormatException;
import com.example.taskmanagementservice.jwt.service.JWTService;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    public AuthenticationResponse login(User user) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException("Invalid username or password");
        }

        String accessToken = jwtService.generateAccessToken(user.getEmail(),
                Collections.emptyMap());
        String refreshToken = jwtService.createRefreshToken(user.getEmail(),
                Collections.emptyMap());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public AuthenticationResponse refreshToken(
            HttpServletRequest request
    ) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenFormatException("The token must be in the JWT format.");
        }
        String refreshToken = authHeader.substring(7);

        String email = jwtService.extractUserName(refreshToken);
        if (email.isBlank() && email.isEmpty()) {
            throw new InvalidTokenFormatException("The token must contain Email");
        }

        User user = userService.getUserByEmail(email);

        if (!jwtService.validateRefreshToken(refreshToken, user)) {
            throw new InvalidTokenException("The token is incorrect");
        }

        if (jwtService.getCountActiveRefreshTokenByUser(user) > 5) {
            jwtService.revokeAllUserRefreshTokens(user);
        } else {
            jwtService.revokeRefreshToken(refreshToken);
        }

        String newAccessToken = jwtService.generateAccessToken(user.getEmail(),
                Collections.emptyMap());
        String newRefreshToken = jwtService.createRefreshToken(user.getEmail(),
                Collections.emptyMap());


        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
