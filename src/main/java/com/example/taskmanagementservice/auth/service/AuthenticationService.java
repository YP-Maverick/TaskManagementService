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

        return generateAuthResponse(userService.getUserByEmail(user.getEmail()));
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request
    ) {
        String refreshToken = extractTokenFromHeader(request);

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
        return generateAuthResponse(user);
    }

    private AuthenticationResponse generateAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user.getEmail(), user.getId());
        String refreshToken = jwtService.createRefreshToken(user.getEmail(), user.getId());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || header.isBlank() || !header.startsWith("Bearer ")) {
            throw new InvalidTokenFormatException("Invalid authorization header format");
        }
        return header.substring(7);
    }
}
