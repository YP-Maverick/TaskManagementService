package com.example.taskmanagementservice.auth.controller;

import com.example.taskmanagementservice.auth.request.AuthenticationRequest;
import com.example.taskmanagementservice.auth.request.AuthenticationResponse;
import com.example.taskmanagementservice.auth.request.RegistrationRequest;
import com.example.taskmanagementservice.auth.request.RegistrationResponse;
import com.example.taskmanagementservice.auth.service.AuthenticationService;
import com.example.taskmanagementservice.user.model.User;
import com.example.taskmanagementservice.user.model.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Auth",
    description = "Регистрация и аутентификация, обновление токенов, выход")

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;



    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет создать новую учетную запись. Свободный выбор роли",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Регистрация успешна",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegistrationResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(
            @Valid @RequestBody RegistrationRequest request) {
        log.info("Received register. request={}", request);

        User user = userMapper.toUser(request);
        return ResponseEntity.ok(
                userMapper.toRegistrationResponse(
                        authenticationService.register(user)
                )
        );
    }

    @Operation(
            summary = "Логин пользователя",
            description = "Позволяет зарегистрированному пользователю получить access и refresh токены.",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Логин успешен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody AuthenticationRequest request) {
        log.info("Received login. request={}", request);

        User user = userMapper.toUser(request);
        return ResponseEntity.ok(
                authenticationService.login(user)
        );
    }

    @Operation(
            summary = "Обновление токенов",
            description = "Выдает обновленную пару токенов, переданный refresh токен отзывается",
            security = @SecurityRequirement(name = "Bearer Token Auth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Регистрация успешна",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class)))
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request) {
        log.info("Received refreshToken. request={}", request);

        return  ResponseEntity.ok(
                authenticationService.refreshToken(request)
        );
    }
}
